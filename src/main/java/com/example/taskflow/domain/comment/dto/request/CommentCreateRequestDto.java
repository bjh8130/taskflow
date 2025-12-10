package com.example.taskflow.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequestDto {

    @NotBlank(message = "댓글 내용은 필수입니다")
    private String content;

    private Long parentId; // 대댓글인 경우 부모 댓글 ID
}