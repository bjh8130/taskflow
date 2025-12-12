package com.example.taskflow.domain.activityLog.service;

import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.common.response.CustomPageResponse;
import com.example.taskflow.domain.activityLog.dto.response.ActivityLogGetOneResponseDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public CustomPageResponse<ActivityLogResponseDto> findLogPage(
            int page, int size, String type, Long userId, Long taskId, LocalDate startDate, LocalDate endDate) {

        // 페이징 조정
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 날짜 변환
        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

        // 페이지에 담을 로그 정렬
        Page<ActivityLog> logs = activityLogRepository.search(pageable, type, userId, taskId, start, end);

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

    @Transactional(readOnly = true)
    public List<ActivityLogGetOneResponseDto> findMyLog(Long userId) {

        List<ActivityLog> logs = activityLogRepository.findByUserId(userId);

        List<ActivityLogGetOneResponseDto> result = new ArrayList<>();

        for (ActivityLog log : logs) {
            UserActivityLogResponseDto user = new UserActivityLogResponseDto(
                    log.getUser().getId(),
                    log.getUser().getUsername(),
                    log.getUser().getName()
            );
            result.add(new ActivityLogGetOneResponseDto(
                    log.getId(),
                    log.getUser().getId(),
                    user,
                    log.getType(),
                    log.getTaskId(),
                    log.getDescription(),
                    log.getCreatedAt()
            ));
        }

        return result;
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

        ActivityLog activityLog = new ActivityLog(type, user, dto.getTaskId(), description);

        activityLogRepository.save(activityLog);
    }

    public void createCommentUpdateLog(String type, String description, Object result) {

        CommentUpdateResponseDto dto = (CommentUpdateResponseDto) result;

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ActivityLog activityLog = new ActivityLog(type, user, dto.getTaskId(), description);

        activityLogRepository.save(activityLog);
    }

    public void createCommentDeleteLog(String type, String description, Comment comment) {

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ActivityLog activityLog = new ActivityLog(type, user, comment.getTask().getId(), description);

        activityLogRepository.save(activityLog);
    }

}

