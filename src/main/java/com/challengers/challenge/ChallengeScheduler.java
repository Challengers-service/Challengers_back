package com.challengers.challenge;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.ChallengeStatus;
import com.challengers.challenge.domain.CheckFrequencyType;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.point.repository.PointRepository;
import com.challengers.userchallenge.domain.UserChallenge;
import com.challengers.userchallenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Component
public class ChallengeScheduler {
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final PointRepository pointRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void everyday() {
        boolean isMonday = LocalDate.now().getDayOfWeek().getValue() == 1;

        challengeRepository.updateStatusFromValidateToFinish();

        success();

        fail(isMonday);

        challengeRepository.updateStatusFromInProgressToValidate();

        challengeRepository.updateStatusFromReadyToInProgress();

        challengeRepository.updateRound(isMonday);
    }

    private void success() {
        LocalDate validateStartDate = LocalDate.now().minusDays(7);
        List<Challenge> findChallenges = challengeRepository.findAllByEndDate(validateStartDate);
        for (Challenge challenge : findChallenges) {
            Long successUserCount = userChallengeRepository.updateStatusToSuccess(challenge.getId());
            long reward = (long) Math.floor(challenge.getFailedPoint() * 1.0 / successUserCount);

            if (reward != 0L)
                pointRepository.giveReward(challenge.getId(),reward);

        }
    }

    private void fail(boolean isMonday) {
        List<Long> successUserChallengeIds = userChallengeRepository.getSuccessIds(isMonday);
        userChallengeRepository.updateStatusToFail(successUserChallengeIds);
        for (Long userChallengeId : successUserChallengeIds) {
            UserChallenge userChallenge = userChallengeRepository.getChallengeById(userChallengeId);
            userChallenge.getChallenge().addFailedPoint(userChallenge.getChallenge().getDepositPoint());
        }
    }
}
