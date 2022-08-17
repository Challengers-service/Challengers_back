package com.challengers.point.controller;

import com.challengers.common.WithMockCustomUser;
import com.challengers.common.documentation.DocumentationWithSecurity;
import com.challengers.point.domain.PointTransactionType;
import com.challengers.point.dto.PointTransactionResponse;
import com.challengers.point.dto.PointResponse;
import com.challengers.point.service.PointService;
import com.challengers.testtool.StringToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PointController.class)
public class PointControllerTest extends DocumentationWithSecurity {
    @MockBean PointService pointService;

    @Test
    @WithMockCustomUser
    @DisplayName("나의 현재 포인트를 조회한다.")
    void getMyPoint() throws Exception {
        when(pointService.getMyPoint(any())).thenReturn(new PointResponse(1000L));

        mockMvc.perform(get("/api/point")
                .header("Authorization",StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(PointDocumentation.getMyPoint());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("나의 포인트 내역을 조회한다.")
    void getMyPointHistory() throws Exception {
        PageImpl<PointTransactionResponse> page = new PageImpl<>(
                Arrays.asList(
                    new PointTransactionResponse(-1000L, LocalDateTime.now(), PointTransactionType.DEPOSIT, 200L),
                    new PointTransactionResponse(100L, LocalDateTime.now().minusHours(1L), PointTransactionType.ATTENDANCE, 300L)
                )
                , PageRequest.of(0,6),2);

        when(pointService.getMyPointHistory(any(),any())).thenReturn(page);

        mockMvc.perform(get("/api/point/transaction")
                .header("Authorization",StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(PointDocumentation.getMyPointHistory());
    }
}
