package com.challengers.challenge.controller;

import com.challengers.challenge.dto.ChallengeRequest;
import com.challengers.challenge.service.ChallengeService;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    @PostMapping("/challenge")
    public String createChallenge(@RequestBody ChallengeRequest challengeRequest) {
        challengeService.create(challengeRequest,0L);
        return "wow";
    }
}
