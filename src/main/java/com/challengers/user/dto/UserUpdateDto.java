package com.challengers.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDto {

    private MultipartFile image;
    private String name;
    private String bio;

    @NotNull
    public Boolean isImageChanged;

    @Builder
    public UserUpdateDto(MultipartFile image, String name, String bio, Boolean isImageChanged){
        this.image = image;
        this.name = name;
        this.bio = bio;
        this.isImageChanged = isImageChanged;
    }
}
