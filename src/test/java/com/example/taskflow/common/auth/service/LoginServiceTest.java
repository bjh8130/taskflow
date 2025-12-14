package com.example.taskflow.common.auth.service;

import com.example.taskflow.common.auth.util.JwtUtil;
import com.example.taskflow.common.config.PasswordEncoder;
import com.example.taskflow.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;


class LoginServiceTest {

    // LoginServiceTest를 테스트 할거다

    // 실제로 테스트할 LoginServiceTest를 불러오기
    private LoginService loginService;
    private UserRepository userRepository;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;


//    // LoginServiceTest를 초기화 시켜줄 것이고
//
//    @BeforeEach
//    void setUp() {
//        // Given
//        loginService = new LoginService(userRepository, jwtUtil, passwordEncoder);
//
//        // When
//        ReflectionTestUtils.setField(loginService, "loginService", );
//
//
//        //Then
////        assertEquals(expected, actual)
//    }
//
//    @Test
//    @DisplayName("로그인 기능 시 유효한 사용자인지 검증하고 토큰을 발행한다.")
//    void gener


}