package com.example.taskflow.domain.task.entity;

import com.example.taskflow.common.entity.BaseEntity;
import com.example.taskflow.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Task 도메인을 표현하는 JPA 엔티티입니다.
 * 제목, 내용, 상태, 우선순위, 담당자 등 작업의 주요 속성을 가지며 상태 변경 및 soft delete 기능을 제공합니다.
 */
@Entity
@Getter
@Table(name = "tasks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 20)
    private String status;

    @Column(length = 20)
    private String priority;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignee_id", nullable = false)
    private User user;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completedDate;

    @Column
    private boolean isDeleted = false;

    public Task(String title, String description, String status, String priority, User user, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.user = user;
        this.dueDate = dueDate;
    }


    public void update(String title, String description, String priority, User user, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.user = user;
        this.dueDate = dueDate;
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    public void updateStatus(String status) {
        this.status = status;
        // DONE으로 변경 시 완료 시간 기록
        if ("DONE".equals(status) && this.completedDate == null) {
            this.completedDate = LocalDateTime.now();
        }
        // DONE이 아닌 상태로 변경 시 완료 시간 제거
        else if (!"DONE".equals(status)) {
            this.completedDate = null;
        }
    }

    public boolean isCompleted() {
        return "DONE".equals(this.status);
    }
}
