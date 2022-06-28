package com.challengers.feed.controller;

import com.challengers.feed.dto.LikeResponse;
import com.challengers.feed.dto.PostResponse;
import com.challengers.feed.service.PostService;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/feed/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/all")
    public ResponseEntity<List<PostResponse>> getAllPosts(@PageableDefault(size=12, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    @GetMapping("/challenge/{ChallengePhotoId}")
    public ResponseEntity<PostResponse> getOnePost(@PathVariable("ChallengePhotoId") Long ChallengePhotoId) {
        return ResponseEntity.ok(postService.getOnePost(ChallengePhotoId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getUserPosts(@PathVariable("userId") Long userId, @PageableDefault(size=12, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.getUserPosts(userId, pageable));
    }

    @GetMapping("/following")
    public ResponseEntity<List<PostResponse>> getFollowingPosts(@CurrentUser UserPrincipal userPrincipal, @PageableDefault(size=12, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.getFollowingPosts(userPrincipal.getId(), pageable));
    }
}
