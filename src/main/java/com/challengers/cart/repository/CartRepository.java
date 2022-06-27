package com.challengers.cart.repository;

import com.challengers.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByChallengeIdAndUserId(Long challengeId, Long userId);
}
