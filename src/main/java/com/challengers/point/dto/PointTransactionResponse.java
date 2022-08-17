package com.challengers.point.dto;

import com.challengers.point.domain.PointTransactionType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PointTransactionResponse {
    private Long amount;
    private String createdAt;
    private String type;

    private Long result;

    @QueryProjection
    public PointTransactionResponse(Long amount, LocalDateTime createdAt, PointTransactionType type, Long result) {
        this.amount = amount;
        this.createdAt = createdAt.toString();
        this.type = type.toString();
        this.result = result;
    }
}
