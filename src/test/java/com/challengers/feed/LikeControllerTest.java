package com.challengers.feed;

import com.challengers.common.WithMockCustomUser;
import com.challengers.common.documentation.DocumentationWithSecurity;
import com.challengers.feed.controller.LikeController;
import com.challengers.feed.dto.CommentDto;
import com.challengers.feed.dto.LikeResponse;
import com.challengers.feed.service.LikeService;
import com.challengers.follow.FollowDocumentation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LikeController.class)
public class LikeControllerTest extends DocumentationWithSecurity {

    @MockBean
    LikeService likeService;

    @DisplayName("좋아요 조회")
    @WithMockCustomUser
    @Test
    public void getComment() throws Exception {
        ArrayList<Long> likeList = new ArrayList<Long>();
        likeList.add(1L);
        likeList.add(2L);
        likeList.add(3L);

        LikeResponse likeResponse = LikeResponse.builder()
                .likeList(likeList)
                .build();
        doNothing().when(likeService).createLike(any(),any());

        when(likeService.getLike(any())).thenReturn(likeResponse);

        mockMvc.perform(get("/api/feed/like").header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A"))
                .andExpect(status().isOk())
                .andDo(LikeDocumentation.getLike())
                .andDo(print())
                .andReturn();
    }

    @DisplayName("좋아요 추가")
    @WithMockCustomUser
    @Test
    public void createComment() throws Exception {
        doNothing().when(likeService).createLike(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/feed/like/{challengePhotoId}",1)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(LikeDocumentation.createLike())
                .andDo(print())
                .andReturn();
    }

    @DisplayName("좋아요 취소")
    @WithMockCustomUser
    @Test
    public void deleteComment() throws Exception {
        doNothing().when(likeService).deleteLike(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/feed/like/{challengePhotoId}",1)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A"))
                .andExpect(status().isOk())
                .andDo(LikeDocumentation.deleteLike())
                .andDo(print())
                .andReturn();
    }
}
