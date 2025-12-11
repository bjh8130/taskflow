package com.example.taskflow.domain.teamMember.controller;

import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.teamMember.dto.request.TeamMemberCreateRequestDto;
import com.example.taskflow.domain.teamMember.dto.response.TeamMemberCreateResponseDto;
import com.example.taskflow.domain.teamMember.service.TeamMemberService;
import com.example.taskflow.domain.user.dto.response.UserTeamResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams/{teamId}/members")
@RequiredArgsConstructor
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    /**
     * 팀 멤버 추가 API
     */
    @PostMapping
    public ResponseEntity<GlobalResponse<TeamMemberCreateResponseDto>> addTeamMember(
            @PathVariable Long teamId,
            @RequestBody TeamMemberCreateRequestDto request) {
        TeamMemberCreateResponseDto result = teamMemberService.add(teamId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponse.success(true, "팀 멤버가 추가되었습니다.", result));
    }

    /**
     * 팀 멤버 조회 API
     */
    @GetMapping
    public ResponseEntity<GlobalResponse<List<UserTeamResponseDto>>> getTeamMembers(
            @PathVariable Long teamId) {
        List<UserTeamResponseDto> result = teamMemberService.findMembers(teamId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "팀 멤버 조회 성공", result));
    }

    /**
     * 팀 멤버 삭제 API
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<GlobalResponse<Void>> deleteTeamMembers(
            @PathVariable Long teamId,
            @PathVariable Long userId) {
        teamMemberService.delete(teamId, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "팀 멤버가 제거되었습니다.", null));
    }
}
