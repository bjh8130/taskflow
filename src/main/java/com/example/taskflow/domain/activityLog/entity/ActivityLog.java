package com.example.taskflow.domain.activityLog.entity;

import com.example.taskflow.common.entity.BaseEntity;

import com.example.taskflow.domain.activityLog.enums.LogTypes;
import com.example.taskflow.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "activityLogs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LogTypes type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Long taskId;

    @Column(columnDefinition = "TEXT")
    private String description;

    public ActivityLog(LogTypes type, User user, Long taskId, String description) {
        this.type = type;
        this.user = user;
        this.taskId = taskId;
        this.description = description;
    }

}
