package com.challengers.follow;

import com.challengers.follow.dto.FollowResponse;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/follow")
@RestController
public class FollowController {

    private final FollowService followService;

    @GetMapping("/follower")
    public ResponseEntity<List<FollowResponse>> findAllFollowers(@CurrentUser UserPrincipal userPrincipal){
        return ResponseEntity.ok(followService.findAllFollowers(userPrincipal.getId()));
    }

    @GetMapping("/following")
    public ResponseEntity<List<FollowResponse>> findAllFollowing(@CurrentUser UserPrincipal userPrincipal){
        return ResponseEntity.ok(followService.findAllFollowing(userPrincipal.getId()));
    }

    @PostMapping("/{followId}")
    public ResponseEntity<Void> addFollow(@CurrentUser UserPrincipal userPrincipal, @PathVariable Long followId){
        followService.addFollow(userPrincipal.getId(), followId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{followId}")
    public ResponseEntity<Void> unFollow(@CurrentUser UserPrincipal userPrincipal, @PathVariable Long followId){
        followService.unFollow(userPrincipal.getId(), followId);
        return ResponseEntity.ok().build();
    }
}
