package com.challengers.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User {

    public static final String DEFAULT_IMAGE_URL = "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile.png";
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    @JsonIgnore
    private String password;

    private String image;

    private String bio;

    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    private LocalDate visitTime;

    private Long attendanceCount;

    private Long challengeCount;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    List<Achievement> awards = new ArrayList<>();

    public User update(String name) {
        this.name = name;
        return this;
    }

    public void update(Long challengeCount) {
        this.challengeCount = challengeCount;
    }

    public void update(LocalDate visitTime, Long attendanceCount) {
        this.visitTime = visitTime;
        this.attendanceCount = attendanceCount;
    }

    public void update(String name, String bio, String image){
        this.name = name;
        this.bio = bio;
        this.image = (image.equals(""))? User.DEFAULT_IMAGE_URL : image;
    }

    @Builder
    public User(Long id, String name, String email, String image, String bio, String password, Role role, AuthProvider provider, String providerId
    ,LocalDate visitTime, Long attendanceCount, Long challengeCount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.image = image;
        this.bio = bio;
        this.password = password;
        this.role = role;
        this.visitTime = visitTime;
        this.attendanceCount = attendanceCount;
        this.challengeCount = challengeCount;
        this.provider = provider;
        this.providerId = providerId;
    }
}