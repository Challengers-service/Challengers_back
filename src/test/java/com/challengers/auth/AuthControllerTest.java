package com.challengers.auth;

import com.challengers.auth.dto.AuthDto;
import com.challengers.auth.service.AuthService;
import com.challengers.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation){
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                    .addFilter(new CharacterEncodingFilter("UTF-8", true))
                    .apply(springSecurity())
                    .apply(documentationConfiguration(restDocumentation))
                .build();
        userRepository.deleteAll();
    }

    @DisplayName("회원 가입")
    @Test
    public void signUp() throws Exception {
        //given
        String email = "a@a.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "1234";

        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", email);
        map.put("name", name);
        map.put("password", password);
        map.put("passwordConfirm", passwordConfirm);

        String url = "http://localhost:" + port + "/signup";

        //when
        MvcResult res = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(map)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(AuthDocumentation.signUp())
                .andReturn();

        String result = res.getResponse().getContentAsString();

        //then
        assertThat(result).isEqualTo("회원 가입이 성공적으로 완료되었습니다!");
    }

    @DisplayName("로그인") //이메일 형식에 따라야 합니다.
    @Test
    public void signIn() throws Exception {
        //given
        String email = "a@a.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "1234";

        AuthDto authDto = AuthDto.builder().email(email).name(name).password(password).passwordConfirm(passwordConfirm).build();
        authService.signUp(authDto);

        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", email);
        map.put("password", password);

        //when
        String url = "http://localhost:" + port + "/signin";

        //then
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(map)))
                .andExpect(status().isOk())
                .andDo(AuthDocumentation.signIn())
                .andDo(print());
    }
}