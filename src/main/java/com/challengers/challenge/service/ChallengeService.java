package com.challengers.challenge.service;

import com.challengers.cart.repository.CartRepository;
import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.dto.*;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.challengephoto.repository.ChallengePhotoRepository;
import com.challengers.challengetag.domain.ChallengeTag;
import com.challengers.common.AwsS3Uploader;
import com.challengers.photocheck.repository.PhotoCheckRepository;
import com.challengers.point.domain.PointTransactionType;
import com.challengers.point.service.PointService;
import com.challengers.review.repository.ReviewRepository;
import com.challengers.tag.domain.Tag;
import com.challengers.tag.repository.TagRepository;
import com.challengers.user.domain.Achievement;
import com.challengers.user.domain.Award;
import com.challengers.user.domain.User;
import com.challengers.user.repository.AchievementRepository;
import com.challengers.user.repository.UserRepository;
import com.challengers.userchallenge.ChallengeJoinManager;
import com.challengers.userchallenge.domain.UserChallenge;
import com.challengers.userchallenge.domain.UserChallengeStatus;
import com.challengers.userchallenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final AchievementRepository achievementRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final AwsS3Uploader awsS3Uploader;
    private final CartRepository cartRepository;
    private final PhotoCheckRepository photoCheckRepository;
    private final ChallengePhotoRepository challengePhotoRepository;
    private final ReviewRepository reviewRepository;

    private final PointService pointService;


    @Transactional
    public Long create(ChallengeRequest challengeRequest, Long userId) {
        User host = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        pointService.updatePoint(userId, challengeRequest.getDepositPoint()*-1L, PointTransactionType.DEPOSIT);

        // challenge 시작일, 종료일이 올바르지 않을 경우 에러 반환시켜야함

        List<String> examplePhotoUrls = awsS3Uploader.uploadImages(challengeRequest.getExamplePhotos());

        Challenge challenge = Challenge.create(challengeRequest, host, examplePhotoUrls);
        challengeRepository.save(challenge);

        if (challengeRequest.getTags() != null)
            challengeRequest.getTags()
                    .forEach(tag -> ChallengeTag.associate(challenge,findOrCreateTag(tag)));

        userChallengeRepository.save(UserChallenge.create(challenge,host));

        host.update(host.getChallengeCount() + 1);

        updateChallengeAchievement(host);

        return challenge.getId();
    }

    @Transactional
    public void update(ChallengeUpdateRequest challengeUpdateRequest, Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NoSuchElementException::new);
        if (!challenge.getHost().getId().equals(userId)) throw new RuntimeException("권한이 없는 요청");

        challenge.update(challengeUpdateRequest.getIntroduction());
    }

    @Transactional
    public void delete(Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NoSuchElementException::new);
        if (!challenge.getHost().getId().equals(userId)) throw new RuntimeException("권한이 없는 요청");
        if (userChallengeRepository.countByChallengeId(challengeId) != 1) throw new RuntimeException("삭제 조건에 부합하지 않음 - 챌린지 참여자가 2명 이상 있음");

        awsS3Uploader.deleteImages(challenge.getExamplePhotoUrls());
        UserChallenge userChallenge = userChallengeRepository.findByUserIdAndChallengeId(challenge.getHost().getId(), challengeId).orElseThrow(NoSuchElementException::new);
        photoCheckRepository.findByUserChallengeId(userChallenge.getId()).forEach(photoCheck -> {
            photoCheckRepository.delete(photoCheck);
            challengePhotoRepository.delete(photoCheck.getChallengePhoto());
        });

        userChallengeRepository.delete(userChallenge);

        pointService.updatePoint(userId,(long) challenge.getDepositPoint(), PointTransactionType.CANCEL);

        cartRepository.findByChallengeId(challengeId).forEach(cartRepository::delete);

        reviewRepository.findByChallengeIdAndUserId(challengeId,userId).ifPresent(reviewRepository::delete);

        challengeRepository.delete(challenge);
    }

    @Transactional(readOnly = true)
    public ChallengeDetailResponse findChallenge(Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NoSuchElementException::new);

        List<UserChallenge> userChallenges = userChallengeRepository
                .findByChallengeIdAndStatus(challengeId, UserChallengeStatus.IN_PROGRESS);

        long progress = 0L;

        for (UserChallenge userChallenge : userChallenges) {
            progress += userChallenge.getMaxProgress();
        }
        int maxProgress = ChallengeJoinManager.getMaxProgress(challenge);

        return ChallengeDetailResponse.of(challenge,
                userChallengeRepository.countByChallengeId(challengeId),
                reviewRepository.getStarRatingAverageByChallengeId(challengeId),
                reviewRepository.countByChallengeId(challengeId),
                cartRepository.findByChallengeIdAndUserId(challengeId, userId).isPresent(),
                userChallengeRepository.findByUserIdAndChallengeId(userId,challengeId).isPresent(),
                challenge.getFailedPoint()/(progress+maxProgress)*maxProgress);
    }

    @Transactional
    public void join(Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NoSuchElementException::new);
        if (userChallengeRepository.countByChallengeId(challengeId) == challenge.getUserCountLimit())
            throw new RuntimeException("참여 인원이 가득 찼습니다.");

        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        if (userChallengeRepository.findByUserIdAndChallengeId(userId,challengeId).isPresent())
            throw new RuntimeException("이미 참여하고 있는 챌린지 입니다.");

        if (!ChallengeJoinManager.canJoin(challenge))
            throw new RuntimeException(
                    "다음주 월요일까지 남은 일 수 보다 일주일에 인증해야 하는 횟수가 많기때문에 다음 주에 참여해야 합니다.");

        updateChallengeAchievement(user);

        pointService.updatePoint(userId,challenge.getDepositPoint()*-1L, PointTransactionType.DEPOSIT);

        userChallengeRepository.save(UserChallenge.create(challenge, user));
    }

    @Transactional(readOnly = true)
    public Page<ChallengeResponse> search(ChallengeSearchCondition condition, Pageable pageable, Long userId) {
        return challengeRepository.search(condition, pageable).map(
                challenge -> new ChallengeResponse(
                        challenge,
                    userId != null && cartRepository.findByChallengeIdAndUserId(challenge.getId(), userId).isPresent(),
                        userChallengeRepository.findByUserIdAndChallengeId(userId,challenge.getId()).isPresent(),
                        userChallengeRepository.getProfileImagesLimit2(challenge.getId())
                ));
    }

    private void updateChallengeAchievement(User user){
        if(user.getChallengeCount() == 1){
            Achievement achievement = Achievement.builder()
                    .user(user)
                    .award(Award.ONE_PARTICIPATION)
                    .build();

            achievementRepository.save(achievement);
        } else if(user.getChallengeCount() == 50){
            Achievement achievement = Achievement.builder()
                    .user(user)
                    .award(Award.FIFTY_PARTICIPATION)
                    .build();

            achievementRepository.save(achievement);
        }
    }

    private Tag findOrCreateTag(String tag) {
        Tag findTag = tagRepository.findTagByName(tag).orElse(null);
        if (findTag == null) {
            findTag = new Tag(tag);
            tagRepository.save(findTag);
        }
        return findTag;
    }
}
