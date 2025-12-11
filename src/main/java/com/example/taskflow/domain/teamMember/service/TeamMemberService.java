package com.example.taskflow.domain.teamMember.service;

import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.domain.team.entity.Team;
import com.example.taskflow.domain.team.repository.TeamRepository;
import com.example.taskflow.domain.teamMember.dto.request.TeamMemberCreateRequestDto;
import com.example.taskflow.domain.teamMember.dto.response.TeamMemberCreateResponseDto;
import com.example.taskflow.domain.teamMember.entity.TeamMember;
import com.example.taskflow.domain.teamMember.repository.TeamMemberRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public TeamMemberCreateResponseDto add(Long teamId, TeamMemberCreateRequestDto request) {

        // 팀 존재 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 유저 존재 확인
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 이미 팀에 속한 유저인지 확인
        if(teamMemberRepository.existsByTeamIdAndUserId(teamId, request.getUserId())) {
            throw new CustomException(ErrorCode.TEAM_MEMBER_ALREADY_EXISTS);
        }

        // 팀멤버 생성 및 저장
        TeamMember teamMember = new TeamMember(team, user);
        teamMemberRepository.save(teamMember);

        // 팀멤버 목록 불러오기
        List<User> members = teamMemberRepository.findUserIdByTeamId(team.getId());

        // 결과값 반환
        return new TeamMemberCreateResponseDto(team.getId(), team.getName(), team.getDescription(), team.getCreatedAt(), members);

    }
}
