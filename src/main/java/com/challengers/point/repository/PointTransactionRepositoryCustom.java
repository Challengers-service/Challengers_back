package com.challengers.point.repository;

import com.challengers.point.dto.PointTransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointTransactionRepositoryCustom {
    Page<PointTransactionResponse> getPointHistory(Pageable pageable, Long pointId);
}
