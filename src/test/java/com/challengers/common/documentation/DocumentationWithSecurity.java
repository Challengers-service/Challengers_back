package com.challengers.common.documentation;

import com.challengers.auth.repository.RefreshTokenRepository;
import com.challengers.config.SecurityDocumentationConfig;
import com.challengers.point.repository.PointRepository;
import com.challengers.user.repository.AchievementRepository;
import com.challengers.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@Import(SecurityDocumentationConfig.class)
@ExtendWith(RestDocumentationExtension.class)
public class DocumentationWithSecurity {
    protected MockMvc mockMvc;

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    private AchievementRepository achievementRepository;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @MockBean
    private PointRepository pointRepository;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .apply(springSecurity())
                .build();
    }
}
