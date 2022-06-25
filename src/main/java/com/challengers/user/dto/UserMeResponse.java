package com.challengers.user.dto;

import com.challengers.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserMeResponse {
    private Long id;
    private String email;
    private String name;
    private String avatar;
    private String bio;
    private Long followerCount;
    private Long followingCount;

    @Builder
    public UserMeResponse(User user, Long followerCount, Long followingCount) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.avatar = user.getImage();
        this.bio = user.getBio();
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }
}
