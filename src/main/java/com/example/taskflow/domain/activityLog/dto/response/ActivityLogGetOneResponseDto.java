package com.example.taskflow.domain.activityLog.dto.response;

import com.example.taskflow.domain.activityLog.entity.ActivityLog;
import com.example.taskflow.domain.user.dto.response.UserActivityLogResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ActivityLogGetOneResponseDto {

    private final Long id;
    private final Long userId;
    private final UserActivityLogResponseDto user;
    private final String action;
    private final Long targetId;
    private final String description;
    private final LocalDateTime createdAt;

    public static ActivityLogGetOneResponseDto from(ActivityLog log, UserActivityLogResponseDto user) {
        return new ActivityLogGetOneResponseDto(
                log.getId(),
                log.getUser().getId(),
                user,
                log.getType(),
                log.getTaskId(),
                log.getDescription(),
                log.getCreatedAt()
        );
    }
}
