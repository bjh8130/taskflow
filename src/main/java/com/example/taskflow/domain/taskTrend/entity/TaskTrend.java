package com.example.taskflow.domain.taskTrend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "tasktrends")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskTrend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //state_date도 컬럼에 넣어야하죠!?
    @Column
    private LocalDateTime stateDate;

    @Column
    private int createdCount;

    @Column
    private int inProgressCount;

    @Column
    private int completedCount;

    @Column
    private LocalDateTime recorededAt; // 스펠링이 틀렸남 틀리면 명세도 고치기 꼭꼭!!

    public TaskTrend(LocalDateTime stateDate, int createdCount, int inProgressCount, int completedCount, LocalDateTime recorededAt) {
        this.stateDate = stateDate;
        this.createdCount = createdCount;
        this.inProgressCount = inProgressCount;
        this.completedCount = completedCount;
        this.recorededAt = recorededAt;
    }









}
