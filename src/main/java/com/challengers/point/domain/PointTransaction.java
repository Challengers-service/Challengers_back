package com.challengers.point.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointTransaction {
    @Id @GeneratedValue
    @Column(name = "point_transaction_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "point_id")
    private Point point;

    private Long amount;

    private Long result;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PointTransactionType type;

    public PointTransaction(Point point, Long amount, PointTransactionType type) {
        this.point = point;
        this.amount = amount;
        this.result = point.getPoint() + amount;
        this.type = type;
    }
}
