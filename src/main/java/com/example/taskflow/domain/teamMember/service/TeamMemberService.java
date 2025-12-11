package com.example.taskflow.domain.teamMember.service;

import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.domain.team.entity.Team;
import com.example.taskflow.domain.team.repository.TeamRepository;
import com.example.taskflow.domain.teamMember.dto.request.TeamMemberCreateRequestDto;
import com.example.taskflow.domain.teamMember.dto.response.TeamMemberCreateResponseDto;
import com.example.taskflow.domain.teamMember.entity.TeamMember;
import com.example.taskflow.domain.teamMember.repository.TeamMemberRepository;
import com.example.taskflow.domain.user.dto.response.UserTeamResponseDto;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    /**
     * 팀 멤버 추가 Method
     */
    @Transactional
    public TeamMemberCreateResponseDto add(Long teamId, TeamMemberCreateRequestDto request) {

        // 1. 팀 존재 여부 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 2. 유저 존재 여부 확인
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 3. 이미 팀에 속한 유저인지 확인
        if(teamMemberRepository.existsByTeamIdAndUserId(teamId, request.getUserId())) {
            throw new CustomException(ErrorCode.TEAM_MEMBER_ALREADY_EXISTS);
        }

        // 4. 팀 멤버 생성 및 저장
        TeamMember teamMember = new TeamMember(team, user);
        teamMemberRepository.save(teamMember);

        // 5. 팀멤버 목록 불러오기
        List<User> userLists = teamMemberRepository.findUserByTeamId(team.getId());
        List<UserTeamResponseDto> members = new ArrayList<>();

        for (User userList : userLists) {
            members.add(new UserTeamResponseDto(
                    userList.getId(),
                    userList.getUsername(),
                    userList.getName(),
                    userList.getEmail(),
                    userList.getRole(),
                    userList.getCreatedAt()));
        }

        // 6. 결과 값 반환
        return new TeamMemberCreateResponseDto(team.getId(), team.getName(), team.getDescription(), team.getCreatedAt(), members);

    }

    /**
     * 팀 멤버 조회 Method
     */
    @Transactional(readOnly = true)
    public List<UserTeamResponseDto> findMembers(Long teamId) {

        // 1. 팀 존재 여부 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 2. 팀 멤버 목록 불러오기
        List<User> userLists = teamMemberRepository.findUserByTeamId(team.getId());
        List<UserTeamResponseDto> members = new ArrayList<>();

        for (User userList : userLists) {
            members.add(new UserTeamResponseDto(
                    userList.getId(),
                    userList.getUsername(),
                    userList.getName(),
                    userList.getEmail(),
                    userList.getRole(),
                    userList.getCreatedAt()));
        }

        // 3. 결과 값 반환
        return members;
    }

    /**
     * 팀 멤버 삭제 Method
     */
    @Transactional
    public void delete(Long teamId, Long userId) {

        // 1. 팀 존재 여부 확인
        if (!teamRepository.existsById(teamId)) {
            throw new CustomException(ErrorCode.TEAM_NOT_FOUND);
        }

        // 2. 팀에 속한 멤버인지 확인
        TeamMember teamMember = teamMemberRepository.findByTeamIdAndUserId(teamId, userId)
                        .orElseThrow(() -> new CustomException(ErrorCode.TEAM_MEMBER_NOT_FOUND));

        // 3. 결과 값 반환
        teamMemberRepository.deleteById(teamMember.getId());

    }
}
