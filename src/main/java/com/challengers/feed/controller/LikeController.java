package com.challengers.feed.controller;

import com.challengers.feed.service.LikeService;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feed/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{challengePhotoId}")
    public void createLike(@CurrentUser UserPrincipal userPrincipal, @PathVariable("challengePhotoId") Long challengePhotoId){
        likeService.createLike(userPrincipal.getId(), challengePhotoId);
    }

    @DeleteMapping("/{challengePhotoId}")
    public void deleteLike(@CurrentUser UserPrincipal userPrincipal, @PathVariable("challengePhotoId") Long challengePhotoId){
        likeService.deleteLike(userPrincipal.getId(), challengePhotoId);
    }
}

