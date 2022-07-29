package com.challengers.review.dto;

import com.challengers.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class ReviewResponse {
    private Long reviewId;
    private String title;
    private String content;
    private Float starRating;
    private String createdDate;

    private Long userId;
    private String userName;
    private String profileImageUrl;

    public static ReviewResponse of(Review review) {
        return new ReviewResponse(review.getId(), review.getTitle(), review.getContent(), review.getStarRating(),
                review.getCreatedDateYYYYMMDD(), review.getUser().getId(), review.getUser().getName(), review.getUser().getImage());
    }
}
