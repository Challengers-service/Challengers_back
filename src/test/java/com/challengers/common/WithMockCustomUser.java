package com.challengers.common;

import com.challengers.user.domain.AuthProvider;
import com.challengers.user.domain.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
	long id() default 1L;

	String email() default "a@email.com";

	String image() default "image.com";

	String name() default "hi";

	String password() default "password";

	String bio() default "bio";

	AuthProvider provider() default AuthProvider.local;

	Role roles() default Role.USER;

	String providerId() default "providerID";
}
