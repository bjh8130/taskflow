package com.example.taskflow.domain.user.service;

import com.example.taskflow.common.config.PasswordEncoder;
import com.example.taskflow.domain.user.dto.request.UserCreateRequestDto;
import com.example.taskflow.domain.user.dto.response.UserCreateResponseDto;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_success() {

        // given
        UserCreateRequestDto result = new UserCreateRequestDto();

        ReflectionTestUtils.setField(result, "username", "user");
        ReflectionTestUtils.setField(result, "email", "user@example.com");
        ReflectionTestUtils.setField(result, "password", "useruser1!");
        ReflectionTestUtils.setField(result, "name", "사용자");

        given(userRepository.existsByUsername("user")).willReturn(false);
        given(userRepository.existsByEmail("user@example.com")).willReturn(false);
        given(passwordEncoder.encode("useruser1!")).willReturn("encodedPassword");

        User savedUser = new User("user", "user@example.com", "사용자", "encodedPassword");

        ReflectionTestUtils.setField(savedUser, "id", 1L);

        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        UserCreateResponseDto response = userService.createUser(result);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("user");
        assertThat(response.getEmail()).isEqualTo("user@example.com");

        then(userRepository).should(times(1)).save(any(User.class));
        then(passwordEncoder).should(times(1)).encode("useruser1!");
    }

}