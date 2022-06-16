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
    @Column(name = "example_picture_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    private String photo_url;

    private ExamplePhotoType examplePhotoType;

    public ExamplePhoto(Challenge challenge, String photo_url, ExamplePhotoType examplePhotoType) {
        this.challenge = challenge;
        this.photo_url = photo_url;
        this.examplePhotoType = examplePhotoType;
    }
}
