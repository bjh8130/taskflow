package com.example.taskflow.domain.teamMember.controller;

import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.teamMember.dto.request.TeamMemberCreateRequestDto;
import com.example.taskflow.domain.teamMember.dto.response.TeamMemberCreateResponseDto;
import com.example.taskflow.domain.teamMember.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @PostMapping("/{teamId}/members")
    public ResponseEntity<GlobalResponse<TeamMemberCreateResponseDto>> addTeamMember(
            @PathVariable Long teamId,
            @RequestBody TeamMemberCreateRequestDto request) {
        TeamMemberCreateResponseDto result = teamMemberService.add(teamId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponse.success(true, "팀 멤버가 추가되었습니다.", result));
    }
}
