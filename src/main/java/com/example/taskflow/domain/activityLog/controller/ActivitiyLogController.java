package com.example.taskflow.domain.activityLog.controller;

import com.example.taskflow.common.auth.security.PrincipalDetails;
import com.example.taskflow.common.response.CustomPageResponse;
import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.activityLog.dto.response.ActivityLogGetOneResponseDto;
import com.example.taskflow.domain.activityLog.dto.response.ActivityLogResponseDto;
import com.example.taskflow.domain.activityLog.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivitiyLogController {

    private final ActivityLogService activityLogService;

    @GetMapping
    public ResponseEntity<GlobalResponse<CustomPageResponse<ActivityLogResponseDto>>> readLogAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long taskId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        CustomPageResponse<ActivityLogResponseDto> result = activityLogService.findLogPage(
                page, size, type, userId, taskId, startDate, endDate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "활동 로그 조회 성공", result));
    }

    @GetMapping("/me")
    public ResponseEntity<GlobalResponse<List<ActivityLogGetOneResponseDto>>> readMyLog(
            @AuthenticationPrincipal PrincipalDetails details
            ) {
        Long userId = details.getUser().getId();
        List<ActivityLogGetOneResponseDto> result = activityLogService.findMyLog(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "내 활동 로그 조회 성공", result));
    }
}
