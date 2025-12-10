package com.example.taskflow.domain.task.controller;

import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.task.dto.request.TaskCreateRequestDTO;
import com.example.taskflow.domain.task.dto.response.TaskResponseDto;
import com.example.taskflow.domain.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<GlobalResponse<TaskResponseDto>> createTask(@Valid @RequestBody TaskCreateRequestDTO request) {

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
}

