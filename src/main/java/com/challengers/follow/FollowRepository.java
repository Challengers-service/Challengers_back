package com.challengers.follow;

import com.challengers.follow.domain.Follow;
import com.challengers.follow.dto.FollowResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByToUserAndFromUser(Long toUserId, Long fromUserId);

    @Query(value = "select new com.challengers.follow.dto.FollowResponse(u.id, u.name, u.image) from Follow f INNER JOIN User u ON f.fromUser = u.id where f.toUser = :userId")
    List<FollowResponse> findAllByToUser(@Param("userId") Long userId);

    @Query(value = "select new com.challengers.follow.dto.FollowResponse(u.id, u.name, u.image) from Follow f INNER JOIN User u ON f.toUser = u.id where f.fromUser = :userId")
    List<FollowResponse> findAllByFromUser(@Param("userId") Long userId);
}
