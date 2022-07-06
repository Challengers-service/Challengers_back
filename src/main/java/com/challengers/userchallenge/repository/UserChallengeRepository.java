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

public interface UserChallengeRepository extends JpaRepository<UserChallenge,Long> {
    Optional<UserChallenge> findByUserIdAndChallengeId(Long userId, Long challengeId);
    Long countByChallengeId(Long challengeId);

    List<UserChallenge> findByChallengeId(Long challengeId);

    @Query("select uc from UserChallenge uc left join uc.photoChecks pc where uc.status=2 group by uc.id having count(pc.id) < uc.challenge.checkTimesPerRound")
    List<UserChallenge> findAllFail();

    List<UserChallenge> findByChallengeIdAndStatus(Long challengeId, UserChallengeStatus status);
}
