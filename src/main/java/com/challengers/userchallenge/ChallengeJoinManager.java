package com.challengers.userchallenge;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.CheckFrequencyType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class ChallengeJoinManager {
    public static int getMaxProgress(Challenge challenge) {
        LocalDate now = LocalDate.now();
        if (now.isBefore(challenge.getStartDate())) return 100;
        long periodDays = ChronoUnit.DAYS.between(challenge.getStartDate(), challenge.getEndDate());
        CheckFrequencyType checkFrequencyType = challenge.getCheckFrequencyType();
        if (checkFrequencyType.equals(CheckFrequencyType.EVERY_DAY))
            return (int) Math.floor(ChronoUnit.DAYS.between(LocalDate.now(),challenge.getEndDate()) * 1.0
                    / periodDays * 100);
        else return (int) Math.floor(Math.floor((ChronoUnit.DAYS.between(LocalDate.now(),challenge.getEndDate())) / 7.0)
                / (periodDays/7.0) * 100);
    }

    public static boolean canJoin(Challenge challenge) {
        return !challenge.getCheckFrequencyType().equals(CheckFrequencyType.OTHERS)
                || ChronoUnit.DAYS.between(LocalDate.now(),
                LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)))
                >= challenge.getCheckTimesPerRound();
    }
}
