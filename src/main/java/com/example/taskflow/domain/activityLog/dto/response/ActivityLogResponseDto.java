package com.example.taskflow.domain.activityLog.dto.response;

import com.example.taskflow.domain.activityLog.entity.ActivityLog;
import com.example.taskflow.domain.activityLog.enums.LogTypes;
import com.example.taskflow.domain.user.dto.response.UserActivityLogResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ActivityLogResponseDto {

    private final Long id;
    private final LogTypes type;
    private final Long userId;
    private final UserActivityLogResponseDto user;
    private final Long taskId;
    private final LocalDateTime timestamp;
    private final String description;

    public static ActivityLogResponseDto from(ActivityLog log, UserActivityLogResponseDto user) {
        return new ActivityLogResponseDto(
                log.getId(),
                log.getType(),
                log.getUser().getId(),
                user,
                log.getTaskId(),
                log.getCreatedAt(),
                log.getDescription()
        );
    }
}
