package com.challengers.security.oauth;

import com.challengers.common.exception.OAuth2AuthenticationProcessingException;
import com.challengers.security.UserPrincipal;
import com.challengers.security.oauth.user.OAuth2UserInfo;
import com.challengers.security.oauth.user.OAuth2UserInfoFactory;
import com.challengers.user.domain.AuthProvider;
import com.challengers.user.domain.Role;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * OAuth2 로그인으로 가져온 정보를 기반으로 DB에 저장, 업데이트한다.
 * */
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            log.info("OAuth2.0 로그인 프로세스 시작");
            return processOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException ex){
            throw ex;
        }catch (Exception ex){
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    // 사용자 정보 추출
    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User){
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuthUSerInfo(
                                                    userRequest.getClientRegistration().getRegistrationId(),
                                                    oAuth2User.getAttributes()
                                            );
        log.info("OAuth Client Registration ID:" + userRequest.getClientRegistration().getRegistrationId());
        log.info("oAuth2UserInfo.getAttributes()" + oAuth2UserInfo.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())){
            log.error("OAuth2 공급자에서 이메일을 찾을 수 없습니다.");
            throw new OAuth2AuthenticationProcessingException("OAuth2 공급자에서 이메일을 찾을 수 없습니다.");
        }
        log.info("로그인 시도 이메일:" + oAuth2UserInfo.getEmail());
        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()){
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()))){
                throw  new OAuth2AuthenticationProcessingException(user.getProvider() + "계정을 사용하기 위해서 로그인을 해야합니다.");
            }
            //이미 존재하는 회원 사이트로 부터 변경된 정보 업데이트.
            //user = updateExistingUser(user, oAuth2UserInfo);
        }else{
            log.info("새로운 회원 등록");
            user = registerNewUser(userRequest, oAuth2UserInfo);
        }
        System.out.println("User Attributes");
        for (String key : oAuth2User.getAttributes().keySet()) {
            System.out.println(key + ": " + oAuth2User.getAttributes().get(key));
        }


        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {

        return userRepository.save(User.builder()
                .name(oAuth2UserInfo.getName())
                .role(Role.USER)
                .email(oAuth2UserInfo.getEmail())
                .image(User.DEFAULT_IMAGE_URL)
                .attendanceCount(0L)
                .challengeCount(0L)
                .provider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                .providerId(oAuth2UserInfo.getId())
                .build()
        );
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        return userRepository.save(existingUser
                .update(
                        oAuth2UserInfo.getName()
                )
        );
    }


}
