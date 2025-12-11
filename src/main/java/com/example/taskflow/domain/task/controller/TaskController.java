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

/**
 * Task 리소스에 대한 생성, 조회, 수정, 삭제 및 상태 변경 API를 제공하는 REST 컨트롤러입니다.
 * 비즈니스 로직은 TaskService에 위임하며, 모든 응답은 GlobalResponse 포맷으로 반환합니다.
 * 작업 단건·목록 조회, 페이징, 상태 변경 등 Task 관리 기능의 진입점을 담당합니다.
 */
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

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<TaskResponseDto>> getOneTask(@PathVariable Long id) {
        TaskResponseDto result = taskService.getTaskById(id);
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

    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<TaskResponseDto>> updateTask(
            @PathVariable Long id,
            @RequestBody TaskUpdateRequestDto request
        ) {
        TaskResponseDto result = taskService.updateTask(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "작업이 수정되었습니다.", result));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<Void>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "작업이 삭제되었습니다.", null));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<GlobalResponse<TaskResponseDto>> updateTaskStatus(
            @PathVariable Long id,
            @RequestBody TaskStatusRequestDto request
    ) {
        TaskResponseDto result = taskService.updateTaskStatus(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "작업 상태가 변경되었습니다.", result));
    }
}

