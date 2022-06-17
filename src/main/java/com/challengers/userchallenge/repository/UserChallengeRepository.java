package com.challengers.userchallenge.repository;

import com.challengers.userchallenge.domain.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChallengeRepository extends JpaRepository<UserChallenge,Long> {
}
