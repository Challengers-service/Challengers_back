package com.challengers.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class LogInRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @Builder
    public LogInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}