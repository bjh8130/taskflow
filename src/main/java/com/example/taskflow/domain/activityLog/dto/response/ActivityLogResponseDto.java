package com.example.taskflow.domain.activityLog.dto.response;

import com.example.taskflow.domain.user.dto.response.UserActivityLogResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ActivityLogResponseDto {

    private final Long id;
    private final String type;
    private final Long userId;
    private final UserActivityLogResponseDto user;
    private final Long taskId;
    private final LocalDateTime timestamp;
    private final String description;

    public ActivityLogResponseDto(
            Long id,
            String type,
            Long userId,
            UserActivityLogResponseDto user,
            Long taskId,
            LocalDateTime timestamp,
            String description) {
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.user = user;
        this.taskId = taskId;
        this.timestamp = timestamp;
        this.description = description;
    }
}
