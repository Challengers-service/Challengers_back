package com.challengers.user.dto;

import com.challengers.user.domain.Award;
import com.challengers.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserMeResponse {
    private Long id;
    private String email;
    private String name;
    private String image;
    private String bio;
    private Long followerCount;
    private Long followingCount;
    private List<Award> awardList;

    @Builder
    public UserMeResponse(User user, Long followerCount, Long followingCount, List<Award> awardList) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.image = user.getImage();
        this.bio = user.getBio();
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.awardList = awardList;
    }
}
