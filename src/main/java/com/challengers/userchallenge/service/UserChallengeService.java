package com.challengers.userchallenge.service;

import com.challengers.userchallenge.domain.UserChallenge;
import com.challengers.userchallenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserChallengeService {
    private final UserChallengeRepository userChallengeRepository;

    @Transactional
    public void failProcess() {
        List<UserChallenge> fails = userChallengeRepository.findAllFail();
        for (UserChallenge fail : fails) {
            //실패시 프로세스
        }
    }
}