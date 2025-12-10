package com.example.taskflow.domain.comment.entity;

import com.example.taskflow.common.entity.BaseEntity;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * 댓글을 그룹별로 조회
     * 예시: groupId = 1
     * 댓글 A와 그 대댓글들 (A-1,A-2...)
     */
    @Column(name = "group_id")
    private Long groupId;

    /**
     * 댓글 순서용 필드
     * 같은 그룹 내에서의 댓글 순서
     * 코드에서 직접 계산해서 넣음
     * DB 시퀀스랑 다른 개념임!! 헷갈릴 수 있어요.
     */
    @Column(name = "sequence")
    private Long sequence;

    //대댓글 깊이 설정
    @Column(name = "depth")
    @Min(0)
    @Max(1)
    private Long depth;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> children = new ArrayList<>();

    public Comment(String content, Task task, Comment comment, User user) {
        this.content = content;
        this.task = task;
        this.comment = comment;
        this.user = user;
    }
}
