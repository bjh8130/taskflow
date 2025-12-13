package com.example.taskflow.domain.comment.controller;

import com.example.taskflow.common.auth.security.PrincipalDetails;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글/대댓글 생성
     * POST /api/tasks/{taskId}/comments
     * - parentId가 없으면 최상위 댓글 생성
     * - parentId가 있으면 대댓글 생성
     */
    @PostMapping
    public ResponseEntity<GlobalResponse<CommentResponseDto>> createComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentCreateRequestDto request,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long userId = principalDetails.getUser().getId();

        // Service에서 parentId 유무로 자동 판단
        CommentResponseDto result = commentService.createComment(request, taskId, userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponse.success(true, "댓글 생성 성공", result));
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
                .status(HttpStatus.OK)
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
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long userId = principalDetails.getUser().getId();
        CommentUpdateResponseDto result = commentService.updateComment(taskId, commentId, request, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true,"댓글이 수정되었습니다.",result));
    }

    /**
     * 댓글 삭제
     * DELETE /api/tasks/{taskId}/comments/{commentId}
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<GlobalResponse<Void>> deleteComment(
            @PathVariable long taskId,
            @PathVariable long commentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long userId = principalDetails.getUser().getId();
        commentService.deleteComment(commentId, userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "댓글이 삭제되었습니다.", null));
    }
}