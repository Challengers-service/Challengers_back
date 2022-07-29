package com.challengers.point.service;

import com.challengers.point.domain.Point;
import com.challengers.point.domain.PointHistory;
import com.challengers.point.domain.PointHistoryType;
import com.challengers.point.dto.PointHistoryResponse;
import com.challengers.point.dto.PointResponse;
import com.challengers.point.repository.PointHistoryRepository;
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
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional(readOnly = true)
    public PointResponse getMyPoint(Long userId) {
        return new PointResponse(
                pointRepository.findByUserId(userId)
                        .orElseThrow(NoSuchElementException::new)
                        .getPoint()
        );
    }

    @Transactional(readOnly = true)
    public Page<PointHistoryResponse> getMyPointHistory(Pageable pageable, Long userId) {
        Point point = pointRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);

        return pointHistoryRepository.getPointHistory(pageable, point.getId());
    }

    @Transactional
    public void updatePoint(Long userId, Long pointHistory, PointHistoryType type) {
        Point point = pointRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);
        if (pointHistory < 0L && point.getPoint() < pointHistory*-1)
            throw new RuntimeException("포인트가 부족합니다.");
        pointHistoryRepository.save(new PointHistory(point,pointHistory, type));
        point.updatePoint(pointHistory);
    }
}
