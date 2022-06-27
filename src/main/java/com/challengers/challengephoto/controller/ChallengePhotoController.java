package com.challengers.challengephoto.controller;

import com.challengers.challengephoto.dto.ChallengePhotoRequest;
import com.challengers.challengephoto.service.ChallengePhotoService;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/challenge_photo")
@RequiredArgsConstructor
public class ChallengePhotoController {
    private final ChallengePhotoService challengePhotoService;

    @PostMapping("/{challenge_id}")
    public ResponseEntity<Void> addChallengePhoto(@ModelAttribute ChallengePhotoRequest challengePhotoRequest,
                                                  @PathVariable(name = "challenge_id") Long challengeId,
                                                  @CurrentUser UserPrincipal user) {
        challengePhotoService.upload(challengePhotoRequest, challengeId, user.getId());

        return ResponseEntity.ok().build();

    }
}
