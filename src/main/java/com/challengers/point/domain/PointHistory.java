package com.challengers.point.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory {
    @Id @GeneratedValue
    @Column(name = "point_history_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "point")
    private Point point;

    private Long pointHistory;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PointHistoryType type;

    public PointHistory(Point point, Long pointHistory, PointHistoryType type) {
        this.point = point;
        this.pointHistory = pointHistory;
        this.type = type;
    }
}
