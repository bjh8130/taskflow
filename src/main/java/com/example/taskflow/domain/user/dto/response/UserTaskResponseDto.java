package com.example.taskflow.domain.user.dto.response;

import com.example.taskflow.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserTaskResponseDto {
    private final Long id;
    private final String username;
    private final String name;

    public static UserTaskResponseDto from(User user) {
        return new UserTaskResponseDto(
                user.getId(),
                user.getUsername(),
                user.getName()
        );
    }
}
