package com.example.taskflow.domain.task.controller;

import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.task.dto.request.TaskCreateRequestDTO;
import com.example.taskflow.domain.task.dto.response.TaskCreateResponseDto;
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
    @PostMapping()
    public ResponseEntity<GlobalResponse<TaskCreateResponseDto>> createTask(@Valid @RequestBody TaskCreateRequestDTO request) {

        TaskCreateResponseDto response = taskService.createComment(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponse.success(true, "Task 생성 성공",response));

    }
    /*
    이런 응답구조로 나옵니다
    {
      "success": true,
      "message": "작업 목록 조회 성공",
      "data": { }
      }
     */
}

