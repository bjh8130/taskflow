package com.example.taskflow.domain.task.controller;

import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.task.dto.response.MyTaskGetResponseDto;
import com.example.taskflow.domain.task.dto.response.StatsGetResponseDto;
import com.example.taskflow.domain.task.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    // 대시보드 통계 조회
    // TODO: JWT 완료 시 PathVariable 제거
    @GetMapping("/stats/{userId}")
    public ResponseEntity<GlobalResponse<StatsGetResponseDto>> getStats(
            @PathVariable long userId
    ) {
        StatsGetResponseDto result = dashboardService.getDashboard(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "대시보드 통계 조회가 완료되었습니다.", result));
    }

    // 내 작업 요약
    // TODO: JWT 완료 시 PathVariable 제거
    @GetMapping("/tasks/{userId}")
    public ResponseEntity<GlobalResponse<MyTaskGetResponseDto>> getMyTasks(@PathVariable long userId) {
        MyTaskGetResponseDto result = dashboardService.getMyTasks(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "내 작업 요약 조회 성공", result));
    }
}
