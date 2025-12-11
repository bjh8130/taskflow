package com.example.taskflow.domain.user.dto.response;

import com.example.taskflow.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTaskResponseDto {
    private final Long id;
    private final String username;
    private final String name;
    private final String email;

    public static UserTaskResponseDto from(User user, boolean includeEmail) {
        return new UserTaskResponseDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                includeEmail ? user.getEmail() : null
        );
    }
}
