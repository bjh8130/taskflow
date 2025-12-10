package com.example.taskflow.domain.comment.dto.response;

import com.example.taskflow.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCommentResponseDto {

    private Long id;
    private String username;
    private String name;

    public static UserCommentResponseDto from(User user) {
        return UserCommentResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }
}