package com.challengers.challenge.repository;

import com.challengers.challenge.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
    List<Challenge> findAllByStartDate(LocalDate startDate);
    List<Challenge> findAllByEndDate(LocalDate EndDate);
}
