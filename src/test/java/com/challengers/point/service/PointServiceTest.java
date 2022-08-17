package com.challengers.point.service;

import com.challengers.point.domain.Point;
import com.challengers.point.domain.PointTransactionType;
import com.challengers.point.dto.PointTransactionResponse;
import com.challengers.point.dto.PointResponse;
import com.challengers.point.repository.PointTransactionRepository;
import com.challengers.point.repository.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {
    @Mock
    PointRepository pointRepository;

    @Mock
    PointTransactionRepository pointTransactionRepository;

    PointService pointService;

    @BeforeEach
    void setUp() {
        pointService = new PointService(pointRepository, pointTransactionRepository);
    }

    @Test
    @DisplayName("나의 포인트를 조회한다.")
    void getMyPoint() {
        when(pointRepository.findByUserId(any())).thenReturn(Optional.of(Point.create(1L)));

        PointResponse response = pointService.getMyPoint(1L);

        assertThat(response.getPoint()).isEqualTo(0L);
    }

    @Test
    @DisplayName("나의 포인트 내역을 조회한다.")
    void getMyPointHistory() {
        PageImpl<PointTransactionResponse> page = new PageImpl<>(Arrays.asList(
                new PointTransactionResponse(100L, LocalDateTime.now().minusHours(9L), PointTransactionType.ATTENDANCE, 600L),
                new PointTransactionResponse(-500L, LocalDateTime.now().minusHours(6L), PointTransactionType.DEPOSIT, 100L),
                new PointTransactionResponse(2000L, LocalDateTime.now().minusHours(3L), PointTransactionType.CANCEL, 2100L),
                new PointTransactionResponse(7430L,LocalDateTime.now(), PointTransactionType.SUCCESS, 9530L)));
        when(pointRepository.findByUserId(any())).thenReturn(Optional.of(Point.create(1L)));
        when(pointTransactionRepository.getPointHistory(any(),any())).thenReturn(page);

        Page<PointTransactionResponse> response = pointService.getMyPointHistory(PageRequest.of(0, 6), 1L);

        assertThat(response).isEqualTo(page);
    }

    @Test
    @DisplayName("나의 포인트를 업데이트(증가) 하는데 성공한다.")
    void updatePoint_success_plus() {
        Point point = Point.builder()
                .point(0L)
                .build();
        when(pointRepository.findByUserId(any())).thenReturn(Optional.of(point));

        pointService.updatePoint(1L, 100L, PointTransactionType.ATTENDANCE);

        verify(pointTransactionRepository).save(any());
        assertThat(point.getPoint()).isEqualTo(100L);
    }

    @Test
    @DisplayName("나의 포인트를 업데이트(감소) 하는데 성공한다.")
    void updatePoint_success_minus() {
        Point point = Point.builder()
                .point(500L)
                .build();
        when(pointRepository.findByUserId(any())).thenReturn(Optional.of(point));

        pointService.updatePoint(1L, -500L, PointTransactionType.DEPOSIT);

        verify(pointTransactionRepository).save(any());
        assertThat(point.getPoint()).isEqualTo(0L);
    }

    @Test
    @DisplayName("나의 포인트를 업데이트(감소) 하는데 실패한다. - 포인트 부족")
    void updatePoint_fail() {
        Point point = Point.builder()
                .point(0L)
                .build();
        when(pointRepository.findByUserId(any())).thenReturn(Optional.of(point));

        assertThatThrownBy(() -> pointService.updatePoint(1L, -500L, PointTransactionType.DEPOSIT))
                .isInstanceOf(RuntimeException.class);
    }
}
