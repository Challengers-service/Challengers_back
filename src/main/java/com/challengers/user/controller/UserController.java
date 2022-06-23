package com.challengers.user.controller;

import com.challengers.common.exception.ResourceNotFoundException;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import com.challengers.user.domain.User;
import com.challengers.user.dto.UserUpdateDto;
import com.challengers.user.repository.UserRepository;
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
    public ResponseEntity<User> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return ResponseEntity.ok(userService.getCurrentUser(userPrincipal.getId()));
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateUser(@CurrentUser UserPrincipal userPrincipal,@Valid @ModelAttribute UserUpdateDto userUpdateDto){
        userService.updateUser(userPrincipal.getId(), userUpdateDto);
        return ResponseEntity.ok().build();
    }
}