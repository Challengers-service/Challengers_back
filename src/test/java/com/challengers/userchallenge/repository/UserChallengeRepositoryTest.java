package com.challengers.userchallenge.repository;

import com.challengers.challenge.domain.Challenge;
import com.challengers.challenge.repository.ChallengeRepository;
import com.challengers.photocheck.domain.PhotoCheck;
import com.challengers.photocheck.repository.PhotoCheckRepository;
import com.challengers.user.domain.AuthProvider;
import com.challengers.user.domain.Role;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import com.challengers.userchallenge.domain.UserChallenge;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserChallengeRepositoryTest {
    @Autowired
    ChallengeRepository challengeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserChallengeRepository userChallengeRepository;
    @Autowired
    PhotoCheckRepository photoCheckRepository;

    User user;
    Challenge challenge;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .role(Role.USER)
                .email("kjs@asf.com")
                .name("asd")
                .providerId("asf")
                .provider(AuthProvider.local)
                .password("asd")
                .build();
        userRepository.save(user);

        challenge = Challenge.builder()
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(10))
                .checkTimesPerRound(1)
                .build();
        challengeRepository.save(challenge);

    }

    @Test
    void findAllFail() {
        UserChallenge userChallenge = UserChallenge.create(challenge, user);
        userChallengeRepository.save(userChallenge);

        List<PhotoCheck> photoChecks = userChallenge.getPhotoChecks();
        PhotoCheck photoCheck = PhotoCheck.builder().userChallenge(userChallenge).build();
        photoCheckRepository.save(photoCheck);
        photoChecks.add(photoCheck);

        List<UserChallenge> allFail = userChallengeRepository.findAllFail();

        Assertions.assertThat(allFail.size()).isEqualTo(0);
    }

    @Test
    void findAllFail2() {
        User user2 = User.builder()
                .role(Role.USER)
                .email("kjs2@asf.com")
                .name("asd")
                .providerId("asf")
                .provider(AuthProvider.local)
                .password("asd")
                .build();
        userRepository.save(user2);

        UserChallenge userChallenge1 = UserChallenge.create(challenge, user);
        userChallengeRepository.save(userChallenge1);
        UserChallenge userChallenge2 = UserChallenge.create(challenge, user2);
        userChallengeRepository.save(userChallenge2);

        List<PhotoCheck> photoChecks = userChallenge1.getPhotoChecks();
        PhotoCheck photoCheck = PhotoCheck.builder().userChallenge(userChallenge1).build();
        photoCheckRepository.save(photoCheck);
        photoChecks.add(photoCheck);

        List<UserChallenge> allFail = userChallengeRepository.findAllFail();

        Assertions.assertThat(allFail.size()).isEqualTo(1);
    }
}