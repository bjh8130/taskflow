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
public class CommentDeleteUserDto {

    private Long id;
    private String username;
    private String name;

    public static CommentDeleteUserDto from(User user) {
        return CommentDeleteUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }
}