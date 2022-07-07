package com.challengers.auth.repository;

import com.challengers.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Query("select r from RefreshToken r where r.userId = :userId")
    Optional<RefreshToken> findByUserId(@Param("userId") Long userId);

    @Query("select r from RefreshToken r where r.accessToken = :accessToken and r.refreshToken = :refreshToken")
    Optional<RefreshToken> findByAccessTokenAndRefreshToken(@Param("accessToken") String accessToken, @Param("refreshToken") String refreshToken);
}