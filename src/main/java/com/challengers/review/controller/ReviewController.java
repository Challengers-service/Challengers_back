package com.challengers.review.controller;

import com.challengers.review.dto.ReviewRequest;
import com.challengers.review.dto.ReviewResponse;
import com.challengers.review.service.ReviewService;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{challengeId}")
    public ResponseEntity<List<ReviewResponse>> showReviews(@PathVariable(name = "challengeId") Long challengeId) {
        return ResponseEntity.ok(reviewService.findReviews(challengeId));
    }

    @PostMapping
    public ResponseEntity<Void> addReview(@Valid @RequestBody ReviewRequest reviewRequest,
                                          @CurrentUser UserPrincipal user) {
        reviewService.create(reviewRequest, user.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable(name = "reviewId") Long reviewId,
                                             @CurrentUser UserPrincipal user) {
        reviewService.delete(reviewId, user.getId());

        return ResponseEntity.ok().build();
    }
}
