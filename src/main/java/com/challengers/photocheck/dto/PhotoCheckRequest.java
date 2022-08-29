package com.challengers.photocheck.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoCheckRequest {
    @NotNull
    private Long challengeId;
    @NotNull
    private MultipartFile photo;
}
