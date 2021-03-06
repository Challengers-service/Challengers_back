package com.challengers.challenge.service;

import com.challengers.cart.repository.CartRepository;
import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.dto.ChallengeDetailResponse;
import com.challengers.challenge.dto.ChallengeRequest;
import com.challengers.challenge.dto.ChallengeResponse;
import com.challengers.challenge.dto.ChallengeUpdateRequest;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.challengetag.domain.ChallengeTag;
import com.challengers.common.AwsS3Uploader;
import com.challengers.examplephoto.repository.ExamplePhotoRepository;
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
    private final ExamplePhotoRepository examplePhotoRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final AwsS3Uploader awsS3Uploader;
    private final CartRepository cartRepository;


    @Transactional
    public Long create(ChallengeRequest challengeRequest, Long userId) {
        User host = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        // host??? ???????????? ????????????????????? ??????????????????
        // challenge ?????????, ???????????? ???????????? ?????? ?????? ?????? ??????????????????

        String imageUrl = "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/challengeDefaultImage.jpg";
        if (challengeRequest.getImage() != null)
            imageUrl = awsS3Uploader.uploadImage(challengeRequest.getImage());
        List<String> examplePhotoUrls = awsS3Uploader.uploadImages(challengeRequest.getExamplePhotos());

        Challenge challenge = Challenge.create(challengeRequest, host, imageUrl, examplePhotoUrls);
        challengeRepository.save(challenge);

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
        if (!challenge.getHost().getId().equals(userId)) throw new RuntimeException("????????? ?????? ??????");

        String imageUrl = challenge.getImageUrl();
        if (challengeUpdateRequest.getImage()!=null) {
            awsS3Uploader.deleteImage(challenge.getImageUrl());
            imageUrl = awsS3Uploader.uploadImage(challengeUpdateRequest.getImage());
        }

        challenge.update(imageUrl,challengeUpdateRequest.getIntroduction());
    }

    @Transactional
    public void delete(Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NoSuchElementException::new);
        if (!challenge.getHost().getId().equals(userId)) throw new RuntimeException("????????? ?????? ??????");
        if (!userChallengeRepository.countByChallengeId(challengeId).equals(1L)) throw new RuntimeException("?????? ????????? ???????????? ?????? - ????????? ???????????? 2??? ?????? ??????");

        awsS3Uploader.deleteImage(challenge.getImageUrl());
        awsS3Uploader.deleteImages(challenge.getExamplePhotoUrls());
        UserChallenge userChallenge = userChallengeRepository.findByUserIdAndChallengeId(challenge.getHost().getId(), challengeId).orElseThrow(NoSuchElementException::new);

        userChallengeRepository.delete(userChallenge);

        //?????? ????????? ?????? ?????? ??????????????? ??????????????? ?????? ????????????

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
                cartRepository.findByChallengeIdAndUserId(challengeId, userId).isPresent(),
                challenge.getFailedPoint()/(progress+maxProgress)*maxProgress);
    }

    @Transactional
    public void join(Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NoSuchElementException::new);
        if (challenge.getUserCount() == challenge.getUserCountLimit())
            throw new RuntimeException("?????? ????????? ?????? ????????????.");

        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        if (userChallengeRepository.findByUserIdAndChallengeId(userId,challengeId).isPresent())
            throw new RuntimeException("?????? ???????????? ?????? ????????? ?????????.");

        if (!ChallengeJoinManager.canJoin(challenge))
            throw new RuntimeException(
                    "????????? ??????????????? ?????? ??? ??? ?????? ???????????? ???????????? ?????? ????????? ??????????????? ?????? ?????? ???????????? ?????????.");

        challenge.joinUser();
        updateChallengeAchievement(user);

        userChallengeRepository.save(UserChallenge.create(challenge, user));
    }

    @Transactional(readOnly = true)
    public Page<ChallengeResponse> findReadyOrInProgressChallenges(Pageable pageable, Long userId) {
        return challengeRepository.findReadyOrInProgressChallenges(pageable).map(challenge -> new ChallengeResponse(challenge,
                userId != null && cartRepository.findByChallengeIdAndUserId(challenge.getId(), userId).isPresent(),
                userChallengeRepository.findByChallengeId(challenge.getId())
                        .stream().map(userChallenge -> userChallenge.getUser().getId())
                        .collect(Collectors.toList())
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
