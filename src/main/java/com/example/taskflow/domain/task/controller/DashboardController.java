package com.example.taskflow.domain.task.controller;

import com.example.taskflow.common.auth.security.PrincipalDetails;
import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.task.dto.response.MyTaskGetResponseDto;
import com.example.taskflow.domain.task.dto.response.StatsGetResponseDto;
import com.example.taskflow.domain.task.dto.weeklyTrend.WeeklyTrendDto;
import com.example.taskflow.domain.task.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    // 대시보드 통계 조회
    @GetMapping("/stats")
    public ResponseEntity<GlobalResponse<StatsGetResponseDto>> getStats(
            @AuthenticationPrincipal PrincipalDetails principal
            ) {
        long userId = principal.getUser().getId();
        StatsGetResponseDto result = dashboardService.getDashboard(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "대시보드 통계 조회가 완료되었습니다.", result));
    }

    // 내 작업 요약
    @GetMapping("/tasks")
    public ResponseEntity<GlobalResponse<MyTaskGetResponseDto>> getMyTasks(@AuthenticationPrincipal PrincipalDetails principal) {
        long userId = principal.getUser().getId();
        MyTaskGetResponseDto result = dashboardService.getMyTasks(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "내 작업 요약 조회 성공", result));
    }

    /**
     * 주간 작업 조회 - 12.11 성주연
     * /api/dashboard/weekly-trend
     */
    @GetMapping("/weekly-trend")
    public ResponseEntity<GlobalResponse<List<WeeklyTrendDto>>> getWeeklyTrend() {
        List<WeeklyTrendDto> result = dashboardService.getWeeklyTrend();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "주간 작업 추세 조회 성공", result));
    }
}
