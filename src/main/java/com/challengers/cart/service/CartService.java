package com.challengers.cart.service;

import com.challengers.cart.domain.Cart;
import com.challengers.cart.repository.CartRepository;
import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.common.exception.NotFoundException;
import com.challengers.common.exception.UnAuthorizedException;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;

    public void put(Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        if (cartRepository.findByChallengeIdAndUserId(challengeId, userId).isPresent())
            throw new IllegalStateException("이미 찜한 챌린지입니다.");

        cartRepository.save(Cart.create(challenge,user));
    }


    public void takeOut(Long challengeId, Long userId) {
        Cart cart = cartRepository.findByChallengeIdAndUserId(challengeId,userId)
                .orElseThrow(() -> new IllegalStateException("찜하기가 안되어 있습니다."));

        if (!cart.getUser().getId().equals(userId))
            throw new UnAuthorizedException("찜하기를 취소할 권한이 없습니다.");

        cartRepository.delete(cart);
    }
}
