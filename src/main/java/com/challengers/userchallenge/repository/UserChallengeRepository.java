package com.challengers.userchallenge.repository;

import com.challengers.userchallenge.domain.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge,Long> {
    Optional<UserChallenge> findByUserIdAndChallengeId(Long userId, Long challengeId);
    Long countByChallengeId(Long challengeId);
}
