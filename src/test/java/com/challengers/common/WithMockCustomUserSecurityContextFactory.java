package com.challengers.common;

import com.challengers.security.UserPrincipal;
import com.challengers.user.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {

        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        User user = User.builder()
                .id(annotation.id())
                .name(annotation.name())
                .email(annotation.email())
                .password(annotation.password())
                .image(annotation.image())
                .bio(annotation.bio())
                .role(annotation.roles())
                .provider(annotation.provider())
                .providerId(annotation.providerId())
                .build();

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        final UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(userPrincipal,
                user.getPassword(),
                userPrincipal.getAuthorities());

        securityContext.setAuthentication(authenticationToken);
        return securityContext;
    }
}
