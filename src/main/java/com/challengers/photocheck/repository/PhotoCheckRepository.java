package com.challengers.photocheck.repository;

import com.challengers.photocheck.domain.PhotoCheck;
import com.challengers.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoCheckRepository extends JpaRepository<PhotoCheck,Long>, PhotoCheckRepositoryCustom {
    Long countByUserChallengeIdAndRound(Long challengeId, Integer round);

    List<PhotoCheck> findByUserChallengeId(Long userChallengeId);
}
