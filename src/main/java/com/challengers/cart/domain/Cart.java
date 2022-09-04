package com.challengers.cart.domain;

import com.challengers.challenge.domain.Challenge;
import com.challengers.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Cart {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Cart(Long id, Challenge challenge, User user) {
        this.id = id;
        this.challenge = challenge;
        this.user = user;
    }

    public static Cart create(Challenge challenge, User user) {
        return Cart.builder()
                .challenge(challenge)
                .user(user)
                .build();
    }
}
