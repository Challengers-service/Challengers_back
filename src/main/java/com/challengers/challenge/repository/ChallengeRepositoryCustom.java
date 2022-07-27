package com.challengers.challenge.repository;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.dto.ChallengeSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChallengeRepositoryCustom {
    Page<Challenge> search(ChallengeSearchCondition condition, Pageable pageable);
}
