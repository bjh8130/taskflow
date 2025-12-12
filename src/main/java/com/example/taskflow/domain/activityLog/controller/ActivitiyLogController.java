package com.example.taskflow.domain.activityLog.controller;

import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.activityLog.dto.response.ActivityLogResponseDto;
import com.example.taskflow.domain.activityLog.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivitiyLogController {

    private final ActivityLogService activityLogService;

    @GetMapping
    public ResponseEntity<GlobalResponse<List<ActivityLogResponseDto>>> readLogAll() {
        List<ActivityLogResponseDto> result = activityLogService.findLogAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "활동 로그 조회 성공", result));
    }
}
