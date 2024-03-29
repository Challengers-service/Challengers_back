package com.challengers.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateRequest {
    @NotBlank
    String title;
    @NotBlank
    String content;
    @NotNull
    float starRating;
}
