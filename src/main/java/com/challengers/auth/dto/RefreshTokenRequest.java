package com.challengers.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;

    @Builder
    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
