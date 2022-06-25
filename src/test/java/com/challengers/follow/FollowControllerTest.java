package com.challengers.follow;

import com.challengers.common.WithMockCustomUser;
import com.challengers.common.documentation.DocumentationWithSecurity;
import com.challengers.follow.dto.FollowResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FollowController.class)
public class FollowControllerTest extends DocumentationWithSecurity {

    private ArrayList<FollowResponse> followList = new ArrayList<FollowResponse>();

    @MockBean
    FollowService followService;

    @BeforeEach
    public void setup(){
        FollowResponse followResponse1 = FollowResponse.builder()
                .id(1L)
                .name("a")
                .image(DEFAULT_IMAGE_URL)
                .build();
        FollowResponse followResponse2 = FollowResponse.builder()
                .id(2L)
                .name("b")
                .image(DEFAULT_IMAGE_URL)
                .build();

        followList.add(followResponse1);
        followList.add(followResponse2);
    }

    @DisplayName("팔로우 조회")
    @WithMockCustomUser
    @Test
    public void findAllFollowers() throws Exception {
        when(followService.findAllFollowers(any())).thenReturn(followList);

        mockMvc.perform(get("/api/follow/follower").header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A"))
                .andExpect(status().isOk())
                .andDo(FollowDocumentation.findAllFollowers())
                .andDo(print())
                .andReturn();
    }

    @DisplayName("팔로잉 조회")
    @WithMockCustomUser
    @Test
    public void findAllFollowing() throws Exception {
        when(followService.findAllFollowing(any())).thenReturn(followList);

        mockMvc.perform(get("/api/follow/following").header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A"))
                .andExpect(status().isOk())
                .andDo(FollowDocumentation.findAllFollowing())
                .andDo(print())
                .andReturn();
    }

    @DisplayName("팔로우 추가")
    @WithMockCustomUser
    @Test
    public void addFollow() throws Exception {

        doNothing().when(followService).addFollow(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/follow/{followId}",1)
                        .header("Authorization", "Bearer yJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(FollowDocumentation.addFollow())
                .andReturn();
    }

    @DisplayName("팔로우 삭제")
    @WithMockCustomUser
    @Test
    public void unFollow() throws Exception {

        doNothing().when(followService).unFollow(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/follow/{followId}",1)
                        .header("Authorization", "Bearer yJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(FollowDocumentation.unFollow())
                .andReturn();
    }
}
