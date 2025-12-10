package com.example.taskflow.domain.comment.controller;

import com.example.taskflow.common.response.CustomPageResponse;
import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.comment.dto.request.CommentCreateRequestDto;
import com.example.taskflow.domain.comment.dto.request.CommentUpdateRequestDto;
import com.example.taskflow.domain.comment.dto.response.CommentGetResponseDto;
import com.example.taskflow.domain.comment.dto.response.CommentResponseDto;
import com.example.taskflow.domain.comment.dto.response.CommentUpdateResponseDto;
import com.example.taskflow.domain.comment.service.CommentService;
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

    /**
     * 댓글 목록 조회
     * GET /api/tasks/{taskId}/comments(기본 newest)
     * /api/tasks/2/comments?page=0&size=10&sort=oldest
     */
    @GetMapping
    public ResponseEntity<GlobalResponse<CustomPageResponse<CommentGetResponseDto>>> getComments(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "newest") String sort) {

        CustomPageResponse<CommentGetResponseDto> result = commentService.getComments(taskId, page, size, sort);
        return ResponseEntity
                .ok()
                .body(GlobalResponse.success(true, "댓글 목록을 조회했습니다.", result));
    }

    /**
     * 댓글 수정
     * PUT /api/tasks/{taskId}/comments/{commentId}
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<GlobalResponse<CommentUpdateResponseDto>> updateComment(
            @PathVariable long taskId,
            @PathVariable long commentId,
            @RequestBody CommentUpdateRequestDto request,
            @RequestHeader("userId") Long userId) {

        CommentUpdateResponseDto result = commentService.updateComment(taskId, commentId, request, userId);
        return ResponseEntity
                .ok()
                .body(GlobalResponse.success(true,"댓글이 수정되었습니다.",result));
    }
}