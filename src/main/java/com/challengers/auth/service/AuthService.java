package com.challengers.auth.service;

import com.challengers.auth.dto.AuthDto;
import com.challengers.auth.dto.LogInRequest;
import com.challengers.auth.dto.TokenDto;
import com.challengers.common.exception.BadRequestException;
import com.challengers.security.TokenProvider;
import com.challengers.user.domain.AuthProvider;
import com.challengers.user.domain.Role;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseEntity<String> signUp(@Valid @RequestBody AuthDto authDto) {
        if(userRepository.existsByEmail(authDto.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        userRepository.save(User.builder()
                .name(authDto.getName())
                .email(authDto.getEmail())
                .provider(AuthProvider.local)
                .password(passwordEncoder.encode(authDto.getPassword()))
                .image(User.DEFAULT_IMAGE_URL)
                .role(Role.USER)
                .build()
        );

        return new ResponseEntity<String>("회원 가입이 성공적으로 완료되었습니다!", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<TokenDto> signIn(@Valid @RequestBody LogInRequest logInRequest) {
        User user = userRepository.findByEmail(logInRequest.getEmail()).get();

        String jwt = tokenProvider.createTokenByUserEntity(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto("Bearer " + jwt), httpHeaders, HttpStatus.OK);
    }
}