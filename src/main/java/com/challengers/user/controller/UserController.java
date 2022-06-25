package com.challengers.user.controller;

import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import com.challengers.user.domain.User;
import com.challengers.user.dto.UserMeResponse;
import com.challengers.user.dto.UserUpdateRequest;
import com.challengers.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return ResponseEntity.ok(userService.getCurrentUser(userPrincipal.getId()));
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateUser(@CurrentUser UserPrincipal userPrincipal,@Valid @ModelAttribute UserUpdateRequest userUpdateRequest){
        userService.updateUser(userPrincipal.getId(), userUpdateRequest);
        return ResponseEntity.ok().build();
    }
}