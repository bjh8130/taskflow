package com.example.taskflow.domain.search.dto.response;

import com.example.taskflow.domain.user.entity.User;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class UserSearchDto {
    private final Long id;
    private final String username;
    private final String name;

    public static UserSearchDto from(User user) {
        return new UserSearchDto(
                user.getId(),
                user.getUsername(),
                user.getName()
        );
    }
}
