package com.challengers.point.domain;

import com.challengers.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {
    @Id @GeneratedValue
    @Column(name = "point_id")
    private Long id;

    private Long userId;
    private Long point;

    public void updatePoint(Long pointHistory) {
        point += pointHistory;
    }
}
