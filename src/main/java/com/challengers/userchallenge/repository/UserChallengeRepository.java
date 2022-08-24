package com.challengers.userchallenge.repository;

import com.challengers.challenge.domain.ChallengeStatus;
import com.challengers.challenge.domain.CheckFrequencyType;
import com.challengers.userchallenge.domain.UserChallenge;
import com.challengers.userchallenge.domain.UserChallengeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge,Long>, UserChallengeRepositoryCustom {
    Optional<UserChallenge> findByUserIdAndChallengeId(Long userId, Long challengeId);
    int countByChallengeId(Long challengeId);

    List<UserChallenge> findByChallengeId(Long challengeId);

    List<UserChallenge> findByChallengeIdAndStatus(Long challengeId, UserChallengeStatus status);
}
