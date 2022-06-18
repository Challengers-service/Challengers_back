package com.challengers.examplephoto.domain;

import com.challengers.challenge.domain.Challenge;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExamplePhoto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "example_photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    private String photo_url;

    public ExamplePhoto(Challenge challenge, String photo_url) {
        this.challenge = challenge;
        this.photo_url = photo_url;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }
}
