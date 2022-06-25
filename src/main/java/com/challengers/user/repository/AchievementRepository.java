package com.challengers.user.repository;

import com.challengers.user.domain.Achievement;
import com.challengers.user.domain.Award;
import com.challengers.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    @Query("select a.award from Achievement a where a.user = :user")
    List<Award> findAllByUser(@Param("user") User user);
}
