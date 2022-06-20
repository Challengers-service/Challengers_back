package com.challengers.user;

import com.challengers.common.WithMockCustomUser;
import com.challengers.common.documentation.DocumentationWithSecurity;
import com.challengers.user.controller.UserController;
import com.challengers.user.domain.Role;
import com.challengers.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import java.util.Optional;

import static com.challengers.user.domain.AuthProvider.local;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest extends DocumentationWithSecurity {
    private User user;

    @BeforeEach
    public void setup(){
        user = User.builder()
                .id(1L)
                .name("a")
                .email("a@a.com")
                .bio("a입니다.")
                .role(Role.USER)
                .image("http://aws-s3-image")
                .provider(local)
                .providerId("abc123")
                .build();
    }

    @DisplayName("내정보 조회")
    @WithMockCustomUser
    @Test
    public void getCurrentUser() throws Exception {

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user/me").header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A"))
                .andExpect(status().isOk())
                .andDo(UserDocumentation.getCurrentUser())
                .andDo(print())
                .andReturn();
    }
}