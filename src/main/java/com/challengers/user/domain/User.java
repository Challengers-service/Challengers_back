package com.challengers.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User {
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

    public User update(String name) {
        this.name = name;
        return this;
    }

    @Builder
    public User(Long id, String name, String email, String image, String bio, String password, Role role, AuthProvider provider, String providerId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.image = image;
        this.bio = bio;
        this.password = password;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}