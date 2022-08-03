package com.challengers.userchallenge;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.CheckFrequencyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

public class ChallengeJoinManagerTest {

    @Test
    @DisplayName("지금 참여시 달성할 수 있는 최대 진행률 계산")
    void getMaxProgress() {
        Challenge challenge1 = Challenge.builder()
                .startDate(LocalDate.now().plusDays(1))
                .checkFrequencyType(CheckFrequencyType.EVERY_DAY)
                .build();

        Challenge challenge2 = Challenge.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .checkFrequencyType(CheckFrequencyType.EVERY_DAY)
                .build();

        Challenge challenge3 = Challenge.builder()
                .startDate(LocalDate.now().minusDays(7))
                .endDate(LocalDate.now().plusDays(21))
                .checkFrequencyType(CheckFrequencyType.EVERY_WEEK)
                .build();

        assertThat(ChallengeJoinManager.getMaxProgress(challenge1)).isEqualTo(100);
        assertThat(ChallengeJoinManager.getMaxProgress(challenge2)).isEqualTo(100);
        assertThat(ChallengeJoinManager.getMaxProgress(challenge3)).isEqualTo((int)Math.floor(3.0/4 * 100));
    }

    @Test
    @DisplayName("챌린지에 참가가 가능한 경우 - 챌린지 인증 빈도가 매일이나 매주인 경우")
    void canJoin_EVERY() {
        Challenge challenge1 = Challenge.builder()
                .checkFrequencyType(CheckFrequencyType.EVERY_DAY)
                .build();

        Challenge challenge2 = Challenge.builder()
                .checkFrequencyType(CheckFrequencyType.EVERY_WEEK)
                .build();

        assertThat(ChallengeJoinManager.canJoin(challenge1)).isTrue();
        assertThat(ChallengeJoinManager.canJoin(challenge2)).isTrue();
    }

    @Test
    @DisplayName("챌린지에 참가가 가능한 경우 - " +
            "챌린지 인증 빈도가 직접 입력이면서 " +
            "다음주 월요일 까지 남은 일 수가 챌린지 인증 횟수보다 크거나 같은 경우")
    void canJoin_OTHER() {
        Challenge challenge = Challenge.builder()
                .checkTimesPerRound(0)
                .checkFrequencyType(CheckFrequencyType.OTHERS)
                .build();

        assertThat(ChallengeJoinManager.canJoin(challenge)).isTrue();
    }

    @Test
    @DisplayName("챌린지에 참가가 불가능한 경우 - " +
            "챌린지 인증 빈도가 직접 입력이면서 " +
            "다음주 월요일 까지 남은 일 수가 챌린지 인증 횟수보다 작은 경우")
    void canJoin_OTHER_fail() {
        Challenge challenge = Challenge.builder()
                .checkTimesPerRound(8)
                .checkFrequencyType(CheckFrequencyType.OTHERS)
                .build();

        assertThat(ChallengeJoinManager.canJoin(challenge)).isFalse();
    }

}
