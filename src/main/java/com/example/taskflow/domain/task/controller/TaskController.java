package com.example.taskflow.domain.task.controller;

import com.example.taskflow.common.response.CustomPageResponse;
import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.task.dto.request.*;
import com.example.taskflow.domain.task.dto.response.TaskResponseDto;
import com.example.taskflow.domain.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<GlobalResponse<TaskResponseDto>> createTask(@Valid @RequestBody TaskCreateRequestDto request) {

        TaskResponseDto result = taskService.createTask(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponse.success(true, "Task 생성 성공", result));

    }

    @GetMapping("{taskId}")
    public ResponseEntity<GlobalResponse<TaskResponseDto>> getOneTask(@PathVariable("taskId") Long taskId) {
        TaskResponseDto result = taskService.getTaskById(taskId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "작업 조회 성공", result));
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<CustomPageResponse<TaskResponseDto>>> getAllTasks(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable,
            @RequestParam(required = false) String status
    ) {
        Page<TaskResponseDto> result = taskService.getAllTask(pageable, status);
        CustomPageResponse<TaskResponseDto> pagingResult = CustomPageResponse.from(result);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "작업 목록 조회 성공",pagingResult));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<GlobalResponse<TaskResponseDto>> updateTask(
            @PathVariable("taskId") Long taskId,
            @RequestBody TaskUpdateRequestDto request
        ) {
        TaskResponseDto result = taskService.updateTask(taskId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "작업이 수정되었습니다.", result));

    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<GlobalResponse<Void>> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "작업이 삭제되었습니다.", null));
    }
}

