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
public class CommentUserReadDto {

    private Long id;
    private String username;
    private String name;
    private String email;
    private String role;

    public static CommentUserReadDto from(User user) {
        return CommentUserReadDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}