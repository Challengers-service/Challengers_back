package com.challengers.cart.service;

import com.challengers.cart.domain.Cart;
import com.challengers.cart.repository.CartRepository;
import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock CartRepository cartRepository;
    @Mock UserRepository userRepository;
    @Mock ChallengeRepository challengeRepository;

    CartService cartService;
    User user;
    Challenge challenge;
    Cart cart;

    @BeforeEach
    void setUp() {
        cartService = new CartService(cartRepository,
                userRepository,
                challengeRepository);

        user = User.builder()
                .id(1L)
                .build();

        challenge = Challenge.builder()
                .host(user)
                .build();

        cart = Cart.create(challenge,user);
    }

    @Test
    @DisplayName("찜하기 성공")
    void put() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        cartService.put(1L,1L);

        verify(cartRepository).save(any());
    }

    @Test
    @DisplayName("찜하기 취소 성공")
    void takeOut() {
        when(cartRepository.findByChallengeIdAndUserId(any(),any())).thenReturn(Optional.of(cart));

        cartService.takeOut(1L,1L);

        verify(cartRepository).delete(any());
    }

    @Test
    @DisplayName("찜하기 취소 실패 - 취소 권한이 없다.")
    void takeOut_fail_unauthorized() {
        when(cartRepository.findByChallengeIdAndUserId(any(),any())).thenReturn(Optional.of(cart));

        Assertions.assertThatThrownBy(()->cartService.takeOut(1L,2L))
                .isInstanceOf(RuntimeException.class);
    }
}