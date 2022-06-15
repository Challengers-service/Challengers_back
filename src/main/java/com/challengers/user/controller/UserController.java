package com.challengers.user.controller;

import com.challengers.common.exception.ResourceNotFoundException;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import com.challengers.user.domain.User;
import com.challengers.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("")
    public User getCurrentUser(@CurrentUser UserPrincipal user) {
        return userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", user.getId()));
    }
}