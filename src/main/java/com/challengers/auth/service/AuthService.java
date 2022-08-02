package com.challengers.auth.service;

import com.challengers.auth.domain.RefreshToken;
import com.challengers.auth.dto.AuthDto;
import com.challengers.auth.dto.LogInRequest;
import com.challengers.auth.dto.TokenDto;
import com.challengers.auth.repository.RefreshTokenRepository;
import com.challengers.common.exception.BadRequestException;
import com.challengers.common.exception.UserException;
import com.challengers.point.domain.Point;
import com.challengers.point.repository.PointRepository;
import com.challengers.security.TokenProvider;
import com.challengers.user.domain.*;
import com.challengers.user.repository.AchievementRepository;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final RefreshTokenRepository refreshTokenRepository;

    private final PointRepository pointRepository;

    @Transactional
    public ResponseEntity<String> signUp(@Valid @RequestBody AuthDto authDto) {
        if(userRepository.existsByEmail(authDto.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        User newUser = userRepository.save(User.builder()
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

        //포인트 테이블 생성
        pointRepository.save(Point.create(newUser.getId()));

        return new ResponseEntity<String>("회원 가입이 성공적으로 완료되었습니다!", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<TokenDto> signIn(@Valid @RequestBody LogInRequest logInRequest) {
        User user = userRepository.findByEmail(logInRequest.getEmail()).orElseThrow(UserException::new);

        //개근30일 업적 로직
        if(Duration.between(user.getVisitTime().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays()==1){
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

        String accessToken = tokenProvider.createAccessTokenByUserEntity(user);
        String refreshToken = tokenProvider.createRefreshTokenByUserEntity(user);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        RefreshToken newRefreshToken = refreshTokenRepository.findByUserId(user.getId())
                .orElseGet(() -> refreshTokenRepository.save(RefreshToken.builder()
                        .refreshToken(refreshToken)
                        .accessToken(accessToken)
                        .userId(user.getId())
                        .build()));

        newRefreshToken.update(accessToken, refreshToken);
        return new ResponseEntity<>(new TokenDto("Bearer " + accessToken, refreshToken), httpHeaders, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<TokenDto> refreshToken(@Valid @RequestBody String accessToken, String refreshToken) {
        RefreshToken findRefreshToken = refreshTokenRepository.findByAccessTokenAndRefreshToken(accessToken, refreshToken).orElseThrow(UserException::new);

        if(findRefreshToken.getUserId() != tokenProvider.getUserIdFromRefreshToken(refreshToken)){// userId가 다르면 fail
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(!tokenProvider.isAccessTokenExpired(accessToken)){ //엑세스 토큰이 만료되지 않으면 fail
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(!tokenProvider.isOverRefreshTokenRenewalHour(refreshToken)){ //리프레시 토큰이 1시간 이상남지 않았으면 fail
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Long userId = findRefreshToken.getUserId();
        User user = userRepository.findById(userId).orElseThrow(UserException::new);

        String newAccessToken = tokenProvider.createAccessTokenByUserEntity(user);
        String newRefreshToken = tokenProvider.createRefreshTokenByUserEntity(user);

        findRefreshToken.update(newAccessToken, newRefreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + newAccessToken);

        return new ResponseEntity<>(new TokenDto("Bearer " + newAccessToken, refreshToken), httpHeaders, HttpStatus.OK);
    }
}