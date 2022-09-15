package com.challengers.challenge.repository;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.dto.ChallengeSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChallengeRepositoryCustom {
    List<Challenge> search(ChallengeSearchCondition condition, Pageable pageable);

    long updateRound(boolean isMonday);

    long updateStatusFromReadyToInProgress();

    long updateStatusFromInProgressToValidate();

    long updateStatusFromValidateToFinish();
}
