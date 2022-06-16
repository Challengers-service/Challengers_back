package com.challengers.challenge.service;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.dto.ChallengeRequest;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.challengetag.domain.ChallengeTag;
import com.challengers.examplephoto.domain.ExamplePhoto;
import com.challengers.examplephoto.domain.ExamplePhotoType;
import com.challengers.examplephoto.repository.ExamplePhotoRepository;
import com.challengers.tag.domain.Tag;
import com.challengers.tag.repository.TagRepository;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ExamplePhotoRepository examplePhotoRepository;

    @Transactional
    public Long create(ChallengeRequest challengeRequest, Long userId) {
        User host = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        Challenge challenge = challengeRequest.toChallenge();
        challenge.setHost(host);
        challengeRepository.save(challenge);

        for(String url : challengeRequest.getGoodExamplePhotoUrls())
            examplePhotoRepository.save(new ExamplePhoto(challenge, url, ExamplePhotoType.GOOD));
        for(String url : challengeRequest.getBadExamplePhotoUrls())
            examplePhotoRepository.save(new ExamplePhoto(challenge, url, ExamplePhotoType.BAD));

        List<String> tags = challengeRequest.getTags();
        for (String tag : tags) {
            ChallengeTag.associate(challenge,findOrCreateTag(tag));
        }

        return challenge.getId();
    }

    @Transactional
    private Tag findOrCreateTag(String tag) {
        Tag findTag = tagRepository.findTagByName(tag).orElse(null);
        if (findTag == null) {
            findTag = new Tag(tag);
            tagRepository.save(findTag);
        }
        return findTag;

    }
}
