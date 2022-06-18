package com.challengers.challenge.controller;

import com.challengers.challenge.dto.ChallengeRequest;
import com.challengers.challenge.dto.ChallengeDetailResponse;
import com.challengers.challenge.service.ChallengeService;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeDetailResponse> findChallenge(@PathVariable Long id) {
        return ResponseEntity.ok(challengeService.findChallenge(id));
    }

    @PostMapping
    public ResponseEntity<Void> createChallenge(@RequestBody ChallengeRequest challengeRequest,
                                          @CurrentUser UserPrincipal user) {
        Long challengeId = challengeService.create(challengeRequest, user.getId());
        return ResponseEntity.created(URI.create("/api/challenge/"+challengeId)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id,
                                  @CurrentUser UserPrincipal user) {
        challengeService.delete(id,user.getId());
        return ResponseEntity.noContent().build();
    }
}
