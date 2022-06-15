package com.challengers.auth.controller;

import com.challengers.auth.dto.AuthDto;
import com.challengers.auth.dto.LogInRequest;
import com.challengers.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody AuthDto authDto){
        return authService.signUp(authDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@Valid @RequestBody LogInRequest logInRequest){
        return authService.signIn(logInRequest);
    }
}
