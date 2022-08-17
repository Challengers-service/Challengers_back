package com.challengers.point.controller;

import com.challengers.point.dto.PointTransactionResponse;
import com.challengers.point.dto.PointResponse;
import com.challengers.point.service.PointService;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
public class PointController {
    private final PointService pointService;

    @GetMapping
    public ResponseEntity<PointResponse> getMyPoint(@CurrentUser UserPrincipal user) {
        return ResponseEntity.ok(pointService.getMyPoint(user.getId()));
    }

    @GetMapping("/transaction")
    public ResponseEntity<Page<PointTransactionResponse>> getMyPointTransaction(Pageable pageable,
                                                                            @CurrentUser UserPrincipal user) {
        return ResponseEntity.ok(pointService.getMyPointHistory(pageable, user.getId()));
    }
}
