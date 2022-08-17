package com.challengers.point.service;

import com.challengers.point.domain.Point;
import com.challengers.point.domain.PointTransaction;
import com.challengers.point.domain.PointTransactionType;
import com.challengers.point.dto.PointTransactionResponse;
import com.challengers.point.dto.PointResponse;
import com.challengers.point.repository.PointTransactionRepository;
import com.challengers.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final PointTransactionRepository pointTransactionRepository;

    @Transactional(readOnly = true)
    public PointResponse getMyPoint(Long userId) {
        return new PointResponse(
                pointRepository.findByUserId(userId)
                        .orElseThrow(NoSuchElementException::new)
                        .getPoint()
        );
    }

    @Transactional(readOnly = true)
    public Page<PointTransactionResponse> getMyPointHistory(Pageable pageable, Long userId) {
        Point point = pointRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);

        return pointTransactionRepository.getPointHistory(pageable, point.getId());
    }

    @Transactional
    public void updatePoint(Long userId, Long pointHistory, PointTransactionType type) {
        Point point = pointRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);
        if (pointHistory < 0L && point.getPoint() < pointHistory*-1)
            throw new RuntimeException("포인트가 부족합니다.");
        pointTransactionRepository.save(new PointTransaction(point,pointHistory, type));
        point.updatePoint(pointHistory);
    }
}
