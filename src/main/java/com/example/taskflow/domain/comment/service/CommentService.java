package com.example.taskflow.domain.comment.service;

import com.example.taskflow.common.annotation.ActivityLog;
import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.common.response.CustomPageResponse;
import com.example.taskflow.domain.activityLog.enums.ActivityTypes;
import com.example.taskflow.domain.comment.dto.request.CommentCreateRequestDto;
import com.example.taskflow.domain.comment.dto.request.CommentUpdateRequestDto;
import com.example.taskflow.domain.comment.dto.response.CommentGetResponseDto;
import com.example.taskflow.domain.comment.dto.response.CommentResponseDto;
import com.example.taskflow.domain.comment.dto.response.CommentUpdateResponseDto;
import com.example.taskflow.domain.comment.entity.Comment;
import com.example.taskflow.domain.comment.repository.CommentRepository;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    /**
     * 댓글/대댓글 생성
     * - parentId가 없으면 최상위 댓글 생성
     * - parentId가 있으면 대댓글 생성
     */
    @Transactional
    @ActivityLog(type = ActivityTypes.COMMENT_CREATED)
    public CommentResponseDto createComment(CommentCreateRequestDto request, Long taskId, Long userId) {
        // 공통: User와 Task 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));

        Comment savedComment;

        // parentId 유무로 댓글/대댓글 자동 판단
        if (request.getParentId() == null) {
            savedComment = createTopLevelComment(request, task, user, taskId);
        } else {
            savedComment = createReplyComment(request, task, user);
        }

        return CommentResponseDto.from(savedComment);
    }

    /**
     * 최상위 댓글 생성 (private helper)
     */
    private Comment createTopLevelComment(CommentCreateRequestDto request, Task task, User user, Long taskId) {
        // 새로운 groupId 생성 (해당 Task의 최대 groupId + 1)
        Long maxGroupId = commentRepository.findMaxGroupIdByTaskId(taskId);
        Long newGroupId = maxGroupId + 1;

        // 댓글 생성
        Comment comment = Comment.builder()
                .content(request.getContent())
                .task(task)
                .user(user)
                .groupId(newGroupId)
                .sequence(0L)
                .depth(0L)
                .build();

        return commentRepository.save(comment);
    }

    /**
     * 대댓글 생성 (private helper)
     */
    private Comment createReplyComment(CommentCreateRequestDto request, Task task, User user) {
        // 부모 댓글 조회
        Comment parent = commentRepository.findById(request.getParentId())
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // depth 제한 확인 (최대 2단계)
        if (parent.getDepth() >= 1) {
            throw new CustomException(ErrorCode.COMMENT_DEPTH_EXCEEDED);
        }

        // 같은 그룹 내 최대 sequence 조회
        Long maxSequence = commentRepository.findMaxSequenceByGroupId(parent.getGroupId());
        Long newSequence = maxSequence + 1;

        // 대댓글 생성
        Comment reply = Comment.builder()
                .content(request.getContent())
                .task(task)
                .user(user)
                .comment(parent)
                .groupId(parent.getGroupId())
                .sequence(newSequence)
                .depth(parent.getDepth() + 1)
                .build();

        return commentRepository.save(reply);
    }

    /**
     * 댓글 목록 조회 (페이징)
     */
    public CustomPageResponse<CommentGetResponseDto> getComments(Long taskId, int page, int size, String sort) {
        // 정렬 방향 결정
        // sequence는 항상 ASC (부모 댓글 먼저, 그 다음 대댓글 순서대로)
        Sort sortBy = sort.equals("oldest")
                ? Sort.by("groupId").ascending().and(Sort.by("sequence").ascending())
                : Sort.by("groupId").descending().and(Sort.by("sequence").ascending());

        Pageable pageable = PageRequest.of(page, size, sortBy);

        // 댓글 조회 (fetch join으로 User, Task 함께 조회)
        Page<Comment> comments = commentRepository.findByTaskIdWithUserAndTask(taskId, pageable);

        // DTO 변환
        Page<CommentGetResponseDto> responseDtos = comments.map(CommentGetResponseDto::from);

        return CustomPageResponse.from(responseDtos);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    @ActivityLog(type = ActivityTypes.COMMENT_UPDATED)
    public CommentUpdateResponseDto updateComment(long taskId, long commentId, CommentUpdateRequestDto request, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 권한 검증: 본인 댓글만 수정 가능
        if (!comment.isAuthor(userId)) {
            throw new CustomException(ErrorCode.COMMENT_FORBIDDEN);
        }

        comment.updateComment(request.getContent());

        Comment updatedComment = commentRepository.save(comment);

        return CommentUpdateResponseDto.from(updatedComment);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    @ActivityLog(type = ActivityTypes.COMMENT_DELETED)
    public void deleteComment(long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 권한 검증: 본인 댓글만 삭제 가능
        if (!comment.isAuthor(userId)) {
            throw new CustomException(ErrorCode.COMMENT_DELETE_FORBIDDEN);
        }

        // 댓글 삭제
        commentRepository.delete(comment);
    }
}
