package com.example.taskflow.common.auth.controller;

import com.example.taskflow.common.auth.dto.request.LoginRequestDto;
import com.example.taskflow.common.auth.dto.response.LoginResponseDto;
import com.example.taskflow.common.auth.service.LoginService;
import com.example.taskflow.common.auth.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginApi(@RequestBody LoginRequestDto requestDto) {
        String jwtToken = loginService.login(requestDto.getUsername());
        return ResponseEntity.ok(new LoginResponseDto(jwtToken));
    }

    // 토근이 유호한지 검증
    // HttpServletRequest : Postman의 윗쪽 부분을 가져오는 개체
    @GetMapping("/validate")
    public ResponseEntity<Boolean> checkValidate(HttpServletRequest request){

        String authorizationHeader = request.getHeader("Authorization");
        // authorizationHeader가 Postman으로 치면 위에있는 Authorization을 가져올건데,
        // 사실 우리는 Bearer와 띄어쓰기 하나는 필요없다. 그래서 substring으로 날려버린다.
        String jwt = authorizationHeader.substring(7);
        // validateToken : Postman에서 보내준 토큰이 비어있는지 확인
        Boolean validate = jwtUtil.validateToken(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(validate);
    }
//    비밀번호확인
//    @PostMapping("/verify-password")











}
