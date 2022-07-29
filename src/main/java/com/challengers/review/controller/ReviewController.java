package com.challengers.review.controller;

import com.challengers.review.dto.ReviewRequest;
import com.challengers.review.dto.ReviewResponse;
import com.challengers.review.dto.ReviewUpdateRequest;
import com.challengers.review.service.ReviewService;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<ReviewResponse>> showReviewsList(@PathVariable(name = "challengeId") Long challengeId,
                                                                Pageable pageable) {
        return ResponseEntity.ok(reviewService.findReviews(pageable, challengeId));
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

    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(@PathVariable(name = "reviewId") Long reviewId,
                                          @RequestBody ReviewUpdateRequest reviewUpdateRequest,
                                          @CurrentUser UserPrincipal user) {
        reviewService.update(reviewId, reviewUpdateRequest, user.getId());

        return ResponseEntity.ok().build();
    }
}
