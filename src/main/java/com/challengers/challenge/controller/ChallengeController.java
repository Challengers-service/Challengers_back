package com.challengers.challenge.controller;

import com.challengers.challenge.dto.*;
import com.challengers.challenge.service.ChallengeService;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeDetailResponse> findChallenge(@PathVariable Long id,
                                                                 @CurrentUser UserPrincipal user) {
        Long userId = user == null ? null : user.getId();
        return ResponseEntity.ok(challengeService.findChallenge(id,userId));
    }

    @GetMapping
    public ResponseEntity<List<ChallengeResponse>> findCanJoinChallenges(ChallengeSearchCondition condition,
                                                                         Pageable pageable,
                                                                         @CurrentUser UserPrincipal user) {
        Long userId = user == null ? null : user.getId();
        return ResponseEntity.ok(challengeService.search(condition, pageable, userId));
    }

    @PostMapping
    public ResponseEntity<Void> createChallenge(@Valid @ModelAttribute ChallengeRequest challengeRequest,
                                          @CurrentUser UserPrincipal user) {
        Long challengeId = challengeService.create(challengeRequest, user.getId());
        return ResponseEntity.created(URI.create("/api/challenge/"+challengeId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateChallenge(@Valid @RequestBody ChallengeUpdateRequest challengeUpdateRequest,
                                                @PathVariable(name = "id") Long challengeId,
                                                @CurrentUser UserPrincipal user) {

        challengeService.update(challengeUpdateRequest, challengeId, user.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id,
                                  @CurrentUser UserPrincipal user) {
        challengeService.delete(id,user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<Void> joinChallenge(@PathVariable Long id,
                                              @CurrentUser UserPrincipal user) {
        challengeService.join(id, user.getId());

        return ResponseEntity.ok().build();
    }
}
