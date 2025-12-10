package com.example.taskflow.domain.task.controller;

import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.task.dto.response.StatsGetResponseDto;
import com.example.taskflow.domain.task.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    // 대시보드 통계 조회
    @GetMapping("/stats")
    public ResponseEntity<GlobalResponse<StatsGetResponseDto>> getStats() {
        StatsGetResponseDto result = dashboardService.getStats();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "대시보드 통계 조회가 완료되었습니다.", result));
    }
}
