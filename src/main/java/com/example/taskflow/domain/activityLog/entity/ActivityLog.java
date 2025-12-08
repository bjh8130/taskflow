package com.example.taskflow.domain.activityLog.entity;

import com.example.taskflow.common.entity.BaseEntity;
import com.example.taskflow.domain.task.entity.Task;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column()
    private String type;

    @Column

}
