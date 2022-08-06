package com.challengers.challenge;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.point.domain.PointHistoryType;
import com.challengers.point.service.PointService;
import com.challengers.userchallenge.domain.UserChallenge;
import com.challengers.userchallenge.domain.UserChallengeStatus;
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
    private final PointService pointService;


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

    //TODO : 포인트 지급을 벌크 업데이트로 변경
    private void success() {
        LocalDate validateStartDate = LocalDate.now().minusDays(7);
        List<Challenge> findChallenges = challengeRepository.findAllByEndDate(validateStartDate);
        for (Challenge challenge : findChallenges) {
            userChallengeRepository.updateStatusToSuccess(challenge.getId());
            Long sumSuccessProgress = userChallengeRepository.getSumSuccessProgress(challenge.getId());

            long rewardUnit = (long) Math.floor(challenge.getFailedPoint() * 1.0 / sumSuccessProgress);

            List<UserChallenge> successUsers = userChallengeRepository.findByChallengeIdAndStatus(challenge.getId(), UserChallengeStatus.SUCCESS);

            for (UserChallenge successUser : successUsers) {
                pointService.updatePoint(successUser.getUser().getId(),rewardUnit * successUser.getMaxProgress() + challenge.getDepositPoint(), PointHistoryType.SUCCESS);
            }

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
