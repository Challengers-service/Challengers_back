package com.challengers.feed.service;

import com.challengers.feed.domain.Like;
import com.challengers.feed.dto.LikeResponse;
import com.challengers.feed.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    @Transactional
    public LikeResponse getLike(Long userId){
        List<Long> likeList = likeRepository.findAllChallengePhotoIdByUserId(userId);
        for(Long id : likeList){
            System.out.println(id);
        }
        LikeResponse likeResponse = LikeResponse.builder()
                .likeList(likeList)
                .build();

        return likeResponse;
    }

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
