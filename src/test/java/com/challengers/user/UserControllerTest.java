package com.challengers.user;

import com.challengers.common.WithMockCustomUser;
import com.challengers.common.documentation.DocumentationWithSecurity;
import com.challengers.follow.dto.FollowResponse;
import com.challengers.user.controller.UserController;
import com.challengers.user.domain.Award;
import com.challengers.user.domain.User;
import com.challengers.user.dto.UserMeResponse;
import com.challengers.user.dto.UserUpdateRequest;
import com.challengers.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.challengers.testtool.UploadSupporter.uploadMockSupport;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest extends DocumentationWithSecurity {
    private UserMeResponse userMeResponse;
    private ArrayList<Award> awardList = new ArrayList<Award>();

    @MockBean
    UserService userService;

    @BeforeEach
    public void setup(){
        awardList.add(Award.ONE_PARTICIPATION);
        awardList.add(Award.FIFTY_PARTICIPATION);
        awardList.add(Award.PERFECT_ATTENDANCE);

        User user = User.builder()
                .id(1L)
                .name("a")
                .email("a@a.com")
                .bio("a입니다.")
                .image(User.DEFAULT_IMAGE_URL)
                .build();
        userMeResponse = UserMeResponse.builder()
                .user(user)
                .followerCount(3L)
                .followingCount(7L)
                .awardList(awardList)
                .build();
    }

    @DisplayName("내정보 조회")
    @WithMockCustomUser
    @Test
    public void getCurrentUser() throws Exception {

        when(userService.getCurrentUser(any())).thenReturn(userMeResponse);

        mockMvc.perform(get("/api/user/me").header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A"))
                .andExpect(status().isOk())
                .andDo(UserDocumentation.getCurrentUser())
                .andDo(print())
                .andReturn();
    }

    @DisplayName("내정보 수정")
    @WithMockCustomUser
    @Test
    public void getUpdateUser() throws Exception {

        doNothing().when(userService).updateUser(any(),any());

        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .name("string")
                .bio("string")
                .isImageChanged(Boolean.TRUE)
                .image(new MockMultipartFile("사진.png", "사진.png", "image/png", "multipart".getBytes()))
                .build();

        mockMvc.perform(uploadMockSupport(multipart("/api/user/me"), userUpdateRequest)
                        .header("Authorization", "Bearer yJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A")
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("PATCH");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(UserDocumentation.updateUser());
    }
}