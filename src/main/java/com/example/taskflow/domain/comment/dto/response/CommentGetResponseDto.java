package com.example.taskflow.domain.comment.dto.response;

import com.example.taskflow.domain.comment.entity.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentGetResponseDto {

    private Long id;
    private String content;
    private Long taskId;
    private Long userId;
    private CommentUserReadDto user;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentGetResponseDto from(Comment comment) {
        return CommentGetResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .taskId(comment.getTask().getId())
                .userId(comment.getUser().getId())
                .user(CommentUserReadDto.from(comment.getUser()))
                .parentId(comment.getComment() != null ? comment.getComment().getId() : null)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
