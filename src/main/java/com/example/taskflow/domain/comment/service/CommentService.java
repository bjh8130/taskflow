package com.example.taskflow.domain.comment.service;

import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.domain.comment.dto.request.CommentCreateRequestDto;
import com.example.taskflow.domain.comment.dto.response.CommentResponseDto;
import com.example.taskflow.domain.comment.entity.Comment;
import com.example.taskflow.domain.comment.repository.CommentRepository;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
     * 댓글 생성 (최상위 댓글)
     */
    @Transactional
    public CommentResponseDto createComment(CommentCreateRequestDto request, Long taskId, Long userId) {
        // User와 Task 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));

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

        Comment savedComment = commentRepository.save(comment);
        return CommentResponseDto.from(savedComment);
    }

    /**
     * 대댓글 생성 (답글)
     */
    @Transactional
    public CommentResponseDto createReply(CommentCreateRequestDto request, Long taskId, Long userId) {
        // 부모 댓글 확인
        if (request.getParentId() == null) {
            throw new CustomException(ErrorCode.PARENT_COMMENT_REQUIRED);
        }

        // User, Task, 부모 댓글 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));
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

        Comment savedReply = commentRepository.save(reply);

        return CommentResponseDto.from(savedReply);
    }
}
