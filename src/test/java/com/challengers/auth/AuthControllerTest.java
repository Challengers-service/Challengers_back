package com.challengers.auth;

import com.challengers.auth.controller.AuthController;
import com.challengers.auth.dto.AuthDto;
import com.challengers.auth.dto.LogInRequest;
import com.challengers.auth.dto.RefreshTokenRequest;
import com.challengers.auth.dto.TokenDto;
import com.challengers.auth.service.AuthService;
import com.challengers.common.documentation.DocumentationWithSecurity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest extends DocumentationWithSecurity {

    @MockBean
    private AuthService authService;

    private ObjectMapper mapper = new ObjectMapper();

    @DisplayName("회원 가입")
    @Test
    public void signUp() throws Exception {
        AuthDto authDto = AuthDto.builder()
                .email("a@a.com")
                .name("A")
                .password("1234")
                .passwordConfirm("1234")
                .build();

        when(authService.signUp(any()))
                .thenReturn(new ResponseEntity<String>("회원 가입이 성공적으로 완료되었습니다!", HttpStatus.CREATED));

        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(authDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(AuthDocumentation.signUp())
                .andReturn();
    }

    @DisplayName("로그인") //이메일 형식에 따라야 합니다.
    @Test
    public void signIn() throws Exception {
        LogInRequest logInRequest = LogInRequest.builder()
                .email("a@a.com")
                .password("1234")
                .build();

        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A";
        String refreshToken = "rkJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU3MTg4MzA2LCJleHAiOjE2NTcxOTE5MDZ9.yN0JecGEWU11aeFwNGUyzhQcyTiZKnmVRx4oV-CiaNp3r_0Fx_oCIEB0gTCbOIkkUEEOGN-4PkhSWVpoHYCRgB";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer" + accessToken);

        when(authService.signIn(any()))
                .thenReturn(new ResponseEntity<>(new TokenDto("Bearer " + accessToken,refreshToken), httpHeaders, HttpStatus.OK));

        mockMvc.perform(post("/api/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(logInRequest)))
                .andExpect(status().isOk())
                .andDo(AuthDocumentation.signIn())
                .andDo(print());
    }

    @DisplayName("엑세스 토큰 재발급")
    @Test
    public void refreshToken() throws Exception {
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A";
        String refreshToken = "rkJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU3MTg4MzA2LCJleHAiOjE2NTcxOTE5MDZ9.yN0JecGEWU11aeFwNGUyzhQcyTiZKnmVRx4oV-CiaNp3r_0Fx_oCIEB0gTCbOIkkUEEOGN-4PkhSWVpoHYCRgB";

        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken(refreshToken)
                .build();

        when(authService.refreshToken(any()))
                .thenReturn(new ResponseEntity<>(new TokenDto("Bearer " + accessToken, refreshToken), HttpStatus.OK));

        mockMvc.perform(post("/api/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isOk())
                .andDo(AuthDocumentation.refresh())
                .andDo(print());
    }
}