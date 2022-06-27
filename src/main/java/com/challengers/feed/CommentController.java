package com.challengers.feed;

import com.challengers.feed.dto.CommentRequest;
import com.challengers.feed.dto.CommentResponse;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/feed/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping ("/{challengePhotoId}")
    public ResponseEntity<CommentResponse> getComment(@PathVariable("challengePhotoId") Long challengePhotoId){
        return ResponseEntity.ok(commentService.getComment(challengePhotoId));
    }

    @PostMapping("/{challengePhotoId}")
    public ResponseEntity<Void> createComment(@CurrentUser UserPrincipal userPrincipal, @PathVariable("challengePhotoId") Long challengePhotoId, @RequestBody CommentRequest commentRequest){
        commentService.createComment(userPrincipal.getId(), challengePhotoId, commentRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentRequest commentRequest){
        commentService.updateComment(commentId, commentRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
