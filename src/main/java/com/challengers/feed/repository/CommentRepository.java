package com.challengers.feed.repository;

import com.challengers.feed.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countByChallengePhotoId(Long challengePhotoId);
    List<Comment> findByChallengePhotoIdOrderByIdDesc(Long challengePhotoId);
}

