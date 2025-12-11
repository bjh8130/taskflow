package com.example.taskflow.domain.user.dto.response;

import com.example.taskflow.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserGetAllResponseDto {

    private final long id;
    private final String username;
    private final String email;
    private final String name;
    private final String role;
    private final LocalDateTime createdAt;

    public static UserGetAllResponseDto from(User user) {
        return new UserGetAllResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }
}
