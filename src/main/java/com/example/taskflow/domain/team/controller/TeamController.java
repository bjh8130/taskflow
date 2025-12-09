package com.example.taskflow.domain.team.controller;

import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.team.dto.request.TeamCreateRequestDTO;
import com.example.taskflow.domain.team.dto.response.TeamCreateResponseDTO;
import com.example.taskflow.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<GlobalResponse<TeamCreateResponseDTO>> teamCreateApi(@Valid @RequestBody TeamCreateRequestDTO request) {
        TeamCreateResponseDTO result = teamService.save(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponse.success(true, "팀이 생성되었습니다.", result));
    }
}