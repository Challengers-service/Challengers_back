package com.challengers.challenge.repository;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.domain.ChallengeStatus;
import com.challengers.challenge.domain.CheckFrequencyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge,Long>, ChallengeRepositoryCustom {
    List<Challenge> findAllByEndDate(LocalDate EndDate);
}
