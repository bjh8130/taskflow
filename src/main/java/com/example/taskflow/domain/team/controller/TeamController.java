package com.example.taskflow.domain.team.controller;

import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.team.dto.request.TeamCreateRequestDto;
import com.example.taskflow.domain.team.dto.request.TeamUpdateRequestDto;
import com.example.taskflow.domain.team.dto.response.TeamCreateResponseDto;
import com.example.taskflow.domain.team.dto.response.TeamReadResponseDto;
import com.example.taskflow.domain.team.dto.response.TeamUpdateResponseDto;
import com.example.taskflow.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<GlobalResponse<TeamCreateResponseDto>> createTeam(@Valid @RequestBody TeamCreateRequestDto request) {
        TeamCreateResponseDto result = teamService.save(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponse.success(true, "팀이 생성되었습니다.", result));
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<TeamReadResponseDto>>> readTeamAll() {
        List<TeamReadResponseDto> result = teamService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "팀 목록 조회 성공", result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<TeamReadResponseDto>> readTeamOne(@PathVariable Long id) {
        TeamReadResponseDto result = teamService.findOne(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "팀 조회 성공", result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<TeamUpdateResponseDto>> updateTeam(
            @PathVariable Long id,
            @RequestBody TeamUpdateRequestDto request) {
        TeamUpdateResponseDto result = teamService.update(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "팀 정보가 수정되었습니다.", result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<Void>> deleteTeam(@PathVariable Long id) {
        teamService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "팀이 삭제되었습니다.", null));
    }
}