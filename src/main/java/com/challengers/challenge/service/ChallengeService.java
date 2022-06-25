package com.challengers.challenge.service;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.dto.ChallengeDetailResponse;
import com.challengers.challenge.dto.ChallengeRequest;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.challengetag.domain.ChallengeTag;
import com.challengers.common.AwsS3Uploader;
import com.challengers.examplephoto.domain.ExamplePhoto;
import com.challengers.examplephoto.repository.ExamplePhotoRepository;
import com.challengers.tag.domain.Tag;
import com.challengers.tag.repository.TagRepository;
import com.challengers.user.domain.Achievement;
import com.challengers.user.domain.Award;
import com.challengers.user.domain.User;
import com.challengers.user.repository.AchievementRepository;
import com.challengers.user.repository.UserRepository;
import com.challengers.userchallenge.domain.UserChallenge;
import com.challengers.userchallenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

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


    @Transactional
    public Long create(ChallengeRequest challengeRequest, Long userId) {
        User host = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        Challenge challenge = challengeRequest.toChallenge();
        challenge.setHost(host);
        String imageUrl = "default";
        if (challengeRequest.getImage() != null)
            imageUrl = awsS3Uploader.uploadImage(challengeRequest.getImage());
        challenge.setImageUrl(imageUrl);
        challenge.addExamplePhotos(awsS3Uploader.uploadImages(challengeRequest.getExamplePhotos()));
        challengeRepository.save(challenge);

        challengeRequest.getTags()
                .forEach(tag -> ChallengeTag.associate(challenge,findOrCreateTag(tag)));

        userChallengeRepository.save(new UserChallenge(challenge,host,false));

        host.update(host.getChallengeCount() + 1);
        if(host.getChallengeCount() == 1){
            Achievement achievement = Achievement.builder()
                    .user(host)
                    .award(Award.ONE_PARTICIPATION)
                    .build();

            achievementRepository.save(achievement);
        } else if(host.getChallengeCount() == 50){
            Achievement achievement = Achievement.builder()
                    .user(host)
                    .award(Award.FIFTY_PARTICIPATION)
                    .build();

            achievementRepository.save(achievement);
        }

        return challenge.getId();
    }

    @Transactional
    public void delete(Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NoSuchElementException::new);
        if (!challenge.getHost().getId().equals(userId)) throw new RuntimeException("권한이 없는 요청");
        if (!userChallengeRepository.countByChallengeId(challengeId).equals(1L)) throw new RuntimeException("삭제 조건에 부합하지 않음 - 챌린지 참여자가 2명 이상 있음");

        awsS3Uploader.deleteImage(challenge.getImageUrl());
        awsS3Uploader.deleteImages(challenge.getExamplePhotoUrls());
        UserChallenge userChallenge = userChallengeRepository.findByUserIdAndChallengeId(challenge.getHost().getId(), challengeId).orElseThrow(NoSuchElementException::new);

        userChallengeRepository.delete(userChallenge);

        //찜한 사람이 있는 경우 찜목록에서 삭제시키고 알림 보내야함

        challengeRepository.delete(challenge);
    }

    @Transactional(readOnly = true)
    public ChallengeDetailResponse findChallenge(Long id) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(NoSuchElementException::new);
        Long userCount = userChallengeRepository.countByChallengeId(id);

        return ChallengeDetailResponse.of(challenge, userCount);
    }

    @Transactional
    public void join(Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NoSuchElementException::new);
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        UserChallenge userChallenge = new UserChallenge(challenge, user, false);
        userChallengeRepository.save(userChallenge);
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
