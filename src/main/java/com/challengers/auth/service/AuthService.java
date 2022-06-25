package com.challengers.auth.service;

import com.challengers.auth.dto.AuthDto;
import com.challengers.auth.dto.LogInRequest;
import com.challengers.auth.dto.TokenDto;
import com.challengers.common.exception.BadRequestException;
import com.challengers.common.exception.UserException;
import com.challengers.security.TokenProvider;
import com.challengers.user.domain.*;
import com.challengers.user.repository.AchievementRepository;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
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
                .visitTime(LocalDate.now())
                .attendanceCount(0L)
                .challengeCount(0L)
                .role(Role.USER)
                .build()
        );

        return new ResponseEntity<String>("회원 가입이 성공적으로 완료되었습니다!", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<TokenDto> signIn(@Valid @RequestBody LogInRequest logInRequest) {
        User user = userRepository.findByEmail(logInRequest.getEmail()).orElseThrow(UserException::new);

        if(Duration.between(user.getVisitTime().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays()==1){ //출석 로직
            user.update(LocalDate.now(), user.getAttendanceCount()+1);
            if(user.getAttendanceCount() >= 30){
                Achievement achievement = Achievement.builder()
                        .user(user)
                        .award(Award.PERFECT_ATTENDANCE)
                        .build();

                achievementRepository.save(achievement);
            }
        }else{
            user.update(LocalDate.now(), user.getAttendanceCount());
        }

        String jwt = tokenProvider.createTokenByUserEntity(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto("Bearer " + jwt), httpHeaders, HttpStatus.OK);
    }
}