package com.example.taskflow.domain.activityLog.service;

import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.common.response.CustomPageResponse;
import com.example.taskflow.domain.activityLog.dto.response.ActivityLogResponseDto;
import com.example.taskflow.domain.activityLog.entity.ActivityLog;
import com.example.taskflow.domain.activityLog.repository.ActivityLogRepository;
import com.example.taskflow.domain.comment.dto.response.CommentResponseDto;
import com.example.taskflow.domain.comment.dto.response.CommentUpdateResponseDto;
import com.example.taskflow.domain.comment.entity.Comment;
import com.example.taskflow.domain.task.dto.response.TaskResponseDto;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.user.dto.response.UserActivityLogResponseDto;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;

    public CustomPageResponse<ActivityLogResponseDto> findLogPage(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<ActivityLog> logs = activityLogRepository.findAll(pageable);

        Page<ActivityLogResponseDto> result = logs.map(log -> {
            UserActivityLogResponseDto user = new UserActivityLogResponseDto(
                    log.getUser().getId(),
                    log.getUser().getUsername(),
                    log.getUser().getName()
            );
            return ActivityLogResponseDto.from(log, user);
        });

        return CustomPageResponse.from(result);
    }

    public void createTaskLog(String type, String description, Object result) {

        TaskResponseDto dto = (TaskResponseDto) result;

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ActivityLog activityLog = new ActivityLog(type, user, dto.getId(), description);

        activityLogRepository.save(activityLog);
    }

    public void createTaskDeleteLog(String type, String description, Task task) {

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ActivityLog activityLog = new ActivityLog(type, user, task.getId(), description);

        activityLogRepository.save(activityLog);
    }

    public void createCommentLog(String type, String description, Object result) {

        CommentResponseDto dto = (CommentResponseDto) result;

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ActivityLog activityLog = new ActivityLog(type, user, dto.getId(), description);

        activityLogRepository.save(activityLog);
    }

    public void createCommentUpdateLog(String type, String description, Object result) {

        CommentUpdateResponseDto dto = (CommentUpdateResponseDto) result;

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ActivityLog activityLog = new ActivityLog(type, user, dto.getId(), description);

        activityLogRepository.save(activityLog);
    }

    public void createCommentDeleteLog(String type, String description, Comment comment) {

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ActivityLog activityLog = new ActivityLog(type, user, comment.getId(), description);

        activityLogRepository.save(activityLog);
    }

}
