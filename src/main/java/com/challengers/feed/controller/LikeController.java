package com.challengers.feed.controller;

import com.challengers.feed.dto.LikeResponse;
import com.challengers.feed.service.LikeService;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feed/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @GetMapping("")
    public ResponseEntity<LikeResponse> getLike(@CurrentUser UserPrincipal userPrincipal){
        return ResponseEntity.ok(likeService.getLike(userPrincipal.getId()));
    }

    @PostMapping("/{challengePhotoId}")
    public ResponseEntity<Void> createLike(@CurrentUser UserPrincipal userPrincipal, @PathVariable("challengePhotoId") Long challengePhotoId){
        likeService.createLike(userPrincipal.getId(), challengePhotoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{challengePhotoId}")
    public ResponseEntity<Void> deleteLike(@CurrentUser UserPrincipal userPrincipal, @PathVariable("challengePhotoId") Long challengePhotoId){
        likeService.deleteLike(userPrincipal.getId(), challengePhotoId);
        return ResponseEntity.ok().build();
    }
}

