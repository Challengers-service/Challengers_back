package com.challengers.feed;

import com.challengers.common.WithMockCustomUser;
import com.challengers.common.documentation.DocumentationWithSecurity;
import com.challengers.feed.controller.CommentController;
import com.challengers.feed.dto.ChallengePhotoUserDto;
import com.challengers.feed.dto.CommentDto;
import com.challengers.feed.dto.CommentRequest;
import com.challengers.feed.dto.CommentResponse;
import com.challengers.feed.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.ArrayList;

import static com.challengers.user.domain.User.DEFAULT_IMAGE_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
public class CommentControllerTest extends DocumentationWithSecurity {

    private CommentResponse commentResponse;
    private ArrayList<CommentDto> comments = new ArrayList<CommentDto>();
    private ObjectMapper mapper = new ObjectMapper();
    private CommentRequest commentRequest;

    @MockBean
    CommentService commentService;

    @BeforeEach
    public void setup(){
        ChallengePhotoUserDto challengePhotoUserDto1 = ChallengePhotoUserDto.builder()
                .id(1L)
                .name("하진우")
                .image(DEFAULT_IMAGE_URL)
                .build();
        CommentDto commentDto1 = CommentDto.builder()
                .id(1L)
                .auth(challengePhotoUserDto1)
                .content("content1")
                .build();
        comments.add(commentDto1);

        ChallengePhotoUserDto challengePhotoUserDto2 = ChallengePhotoUserDto.builder()
                .id(2L)
                .name("김준성")
                .image(DEFAULT_IMAGE_URL)
                .build();
        CommentDto commentDto2 = CommentDto.builder()
                .id(2L)
                .auth(challengePhotoUserDto2)
                .content("content2")
                .build();
        comments.add(commentDto2);

        commentResponse = CommentResponse.builder()
                .comments(comments)
                .commentCnt(2L)
                .build();

        commentRequest = CommentRequest.builder()
                .content("댓글 내용")
                .build();
    }

    @DisplayName("댓글 조회")
    @WithMockCustomUser
    @Test
    public void getComment() throws Exception {
        when(commentService.getComment(any())).thenReturn(commentResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/feed/comment/{challengePhotoId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(CommentDocumentation.getComment())
                .andDo(print())
                .andReturn();
    }

    @DisplayName("댓글 생성")
    @WithMockCustomUser
    @Test
    public void createComment() throws Exception {
        doNothing().when(commentService).createComment(any(),any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/feed/comment/{challengePhotoId}",1)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk())
                .andDo(CommentDocumentation.createComment())
                .andDo(print())
                .andReturn();
    }

    @DisplayName("댓글 수정")
    @WithMockCustomUser
    @Test
    public void updateComment() throws Exception {
        doNothing().when(commentService).updateComment(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/feed/comment/{commentId}",1)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk())
                .andDo(CommentDocumentation.updateComment())
                .andDo(print())
                .andReturn();
    }

    @DisplayName("댓글 삭제")
    @WithMockCustomUser
    @Test
    public void deleteComment() throws Exception {
        doNothing().when(commentService).deleteComment(any());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/feed/comment/{commentId}",1)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A"))
                .andExpect(status().isOk())
                .andDo(CommentDocumentation.deleteComment())
                .andDo(print())
                .andReturn();
    }
}
