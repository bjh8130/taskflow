package com.example.taskflow.domain.comment.controller;

import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.comment.dto.request.CommentCreateRequestDto;
import com.example.taskflow.domain.comment.dto.response.CommentResponseDto;
import com.example.taskflow.domain.comment.service.CommentService;
import com.example.taskflow.domain.task.dto.response.TaskCreateResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 생성
     * POST /api/tasks/{taskId}/comments
     */
    @PostMapping
    public ResponseEntity<GlobalResponse<CommentResponseDto>> createComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentCreateRequestDto request,
            @RequestHeader("userId") Long userId) {

        CommentResponseDto result = commentService.createComment(request, taskId, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponse.success(true, "댓글 생성 성공", result));
    }

    /**
     * 대댓글 생성
     * POST /api/tasks/{taskId}/comments/reply
     */
    @PostMapping("/reply")
    public ResponseEntity<GlobalResponse<CommentResponseDto>> createReply(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentCreateRequestDto request,
            @RequestHeader("userId") Long userId) {

        CommentResponseDto result = commentService.createReply(request, taskId, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponse.success(true, "대댓글 생성 성공", result));
    }
}