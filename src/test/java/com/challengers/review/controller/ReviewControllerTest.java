package com.challengers.review.controller;

import com.challengers.common.WithMockCustomUser;
import com.challengers.common.documentation.DocumentationWithSecurity;
import com.challengers.review.dto.ReviewRequest;
import com.challengers.review.dto.ReviewResponse;
import com.challengers.review.dto.ReviewUpdateRequest;
import com.challengers.review.service.ReviewService;
import com.challengers.testtool.StringToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReviewController.class)
class ReviewControllerTest extends DocumentationWithSecurity {
    @MockBean ReviewService reviewService;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    @WithMockCustomUser
    @DisplayName("챌린지에 작성된 리뷰 조회")
    void showReviews() throws Exception{
        PageImpl<ReviewResponse> page = new PageImpl<>(Arrays.asList(new ReviewResponse(1L, "너무 좋은 챌린지", "여태 해봤던 챌린지 중에 제일 좋았던거 같아요!",
                        4.5f, "2022-06-21", 1L, "김준성",
                        "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile.png"),
                new ReviewResponse(2L, "너무 별로에요", "챌린지 호스트가 인증샷 확인을 잘 안해요.",
                        2.5f, "2022-06-21", 2L, "김성진",
                        "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile.png"))
                , PageRequest.of(0,6),2);
        when(reviewService.findReviews(any(),any())).thenReturn(page);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/reviews/{challengeId}",1)
                        .header("Authorization", StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(ReviewDocumentation.showReviews());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("리뷰 생성")
    void addReview() throws Exception{
        ReviewRequest reviewRequest = new ReviewRequest(1L, "너무 좋은 챌린지",
                "여태 해봤던 챌린지 중에 제일 좋았던거 같아요!", 4.5f);
        mockMvc.perform(post("/api/reviews/")
                .header("Authorization", "Bearer yJzdWIiOiIxIiwiaWF0IjoxN" +
                        "jU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_e" +
                        "Kw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A")
                .content(mapper.writeValueAsString(reviewRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(ReviewDocumentation.addReview());

        verify(reviewService).create(any(),any());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("리뷰 삭제")
    void deleteReview() throws Exception{
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/reviews/{reviewId}", 1)
                .header("Authorization", "Bearer yJzdWIiOiIxIiwiaWF0IjoxN" +
                        "jU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_e" +
                        "Kw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A"))
                .andExpect(status().isOk())
                .andDo(ReviewDocumentation.deleteReview());

        verify(reviewService).delete(any(),any());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("리뷰 수정")
    void putReview() throws Exception{
        ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest(
                "별로 얻는게 없는 챌린지", "얻는게 정말 없어요!", 1.4f);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/reviews/{reviewId}", 1)
                .header("Authorization", "Bearer yJzdWIiOiIxIiwiaWF0IjoxN" +
                        "jU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_e" +
                        "Kw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A")
                .content(mapper.writeValueAsString(reviewUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(ReviewDocumentation.updateReview());

        verify(reviewService).update(any(),any(),any());
    }

}