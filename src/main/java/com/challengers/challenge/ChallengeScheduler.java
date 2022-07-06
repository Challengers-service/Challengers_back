package com.challengers.challenge;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.ChallengeStatus;
import com.challengers.challenge.domain.CheckFrequencyType;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.userchallenge.domain.UserChallenge;
import com.challengers.userchallenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
public class ChallengeScheduler {
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;

    //매일
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void Everyday() {
        challengeStatusUpdateFromValidateToFinish();
        // TODO:성공한 유저들에게 포인트 지급해주어야 함

        challengeStatusUpdateFromInProgressToValidate();
        toFail();
        challengeStatusUpdateFromReadyToInProgress();

        updateRoundEveryDay();

        // 월요일일 경우
        if (LocalDate.now().getDayOfWeek().getValue() == 1)
            updateRoundEveryWeek();
    }

    private void updateRoundEveryDay() {
        challengeRepository.findAllByCheckFrequencyTypeInAndStatus(new ArrayList<>(Collections.singletonList(CheckFrequencyType.EVERY_DAY)),
                ChallengeStatus.IN_PROGRESS).forEach(Challenge::updateRound);
    }

    private void updateRoundEveryWeek() {
        challengeRepository.findAllByCheckFrequencyTypeInAndStatus(new ArrayList<>(Arrays.asList(CheckFrequencyType.OTHERS, CheckFrequencyType.EVERY_WEEK)),
                ChallengeStatus.IN_PROGRESS).forEach(Challenge::updateRound);
    }

    private void toFail() {
        List<UserChallenge> failUserChallenge = userChallengeRepository.findAllFail();
        Map<Challenge,Long> map = new ConcurrentHashMap<>();
        for (UserChallenge userChallenge : failUserChallenge) {
            userChallenge.fail();
            map.put(userChallenge.getChallenge(),map.getOrDefault(userChallenge.getChallenge(), 0L) + 1);
        }
        for (Challenge challenge : map.keySet()) {
            challenge.addFailedPoint(challenge.getDepositPoint()*map.get(challenge));
        }
    }

    private void challengeStatusUpdateFromReadyToInProgress() {
        LocalDate now = LocalDate.now();
        challengeRepository.findAllByStartDate(now).forEach(Challenge::toInProgress);
    }

    private void challengeStatusUpdateFromInProgressToValidate() {
        LocalDate now = LocalDate.now();
        challengeRepository.findAllByEndDate(now).forEach(Challenge::toValidate);
    }

    private void challengeStatusUpdateFromValidateToFinish() {
        LocalDate validateStartDate = LocalDate.now().minusDays(7);
        challengeRepository.findAllByEndDate(validateStartDate).forEach(Challenge::toFinish);
    }
}
