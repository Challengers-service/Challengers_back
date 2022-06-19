package com.challengers.user;

import com.challengers.security.TokenProvider;
import com.challengers.user.domain.Role;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.challengers.user.domain.AuthProvider.local;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TokenProvider tokenProvider;

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

    @DisplayName("내정보 조회")
    @Test
    public void getCurrentUser() throws Exception {
        //given
        User userA = User.builder()
                .name("a")
                .email("a@a.com")
                .bio("a입니다.")
                .role(Role.USER)
                .image("http://aws-s3-image")
                .provider(local)
                .providerId("abc123")
                .build();

        userRepository.save(userA);

        //when
        String url = "http://localhost:" + port + "/user/me";
        String token = tokenProvider.createTokenByUserEntity(userA);

        MvcResult res = mvc.perform(get(url).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(UserDocumentation.getCurrentUser())
                .andDo(print())
                .andReturn();
    }
}