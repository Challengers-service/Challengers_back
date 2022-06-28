package com.challengers.feed.repository;

import com.challengers.feed.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Long countByChallengePhotoId(Long challengePhotoId);
    Like findByUserIdAndChallengePhotoId(Long userId, Long challengePhotoId);

    @Query("select l.challengePhotoId from Like l where l.userId= :userId")
    List<Long> findAllChallengePhotoIdByUserId(@Param("userId") Long userId);
}
