package com.challengers.feed.service;

import com.challengers.feed.domain.Like;
import com.challengers.feed.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    @Transactional
    public void createLike(Long userId, Long challengePhotoId){
        Like like = Like.builder().userId(userId).challengePhotoId(challengePhotoId).build();
        likeRepository.save(like);
    }

    @Transactional
    public void deleteLike(Long userId, Long challengePhotoId){
        Like like = likeRepository.findByUserIdAndChallengePhotoId(userId,challengePhotoId);
        likeRepository.delete(like);
    }
}
