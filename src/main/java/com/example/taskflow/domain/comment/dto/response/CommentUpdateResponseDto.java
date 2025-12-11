package com.example.taskflow.domain.comment.dto.response;

import com.example.taskflow.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateResponseDto {

    private Long id;
    private Long taskId;
    private Long userId;
    private String content;
    private Long parentId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentUpdateResponseDto from(Comment comment) {
        return CommentUpdateResponseDto.builder()
                .id(comment.getId())
                .taskId(comment.getTask().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .parentId(comment.getComment() != null ? comment.getComment().getId() : null)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
