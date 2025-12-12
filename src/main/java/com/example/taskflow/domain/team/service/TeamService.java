package com.example.taskflow.domain.team.service;

import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.domain.team.dto.request.TeamCreateRequestDto;
import com.example.taskflow.domain.team.dto.request.TeamUpdateRequestDto;
import com.example.taskflow.domain.team.dto.response.TeamCreateResponseDto;
import com.example.taskflow.domain.team.dto.response.TeamReadResponseDto;
import com.example.taskflow.domain.team.dto.response.TeamUpdateResponseDto;
import com.example.taskflow.domain.team.entity.Team;
import com.example.taskflow.domain.teamMember.repository.TeamMemberRepository;
import com.example.taskflow.domain.team.repository.TeamRepository;
import com.example.taskflow.domain.user.dto.response.UserTeamResponseDto;
import com.example.taskflow.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    /**
     * 팀 생성 Method
     */
    @Transactional
    public TeamCreateResponseDto save(TeamCreateRequestDto request) {

        // 1. 생성하려는 팀 이름이 중복되는 지 확인
        if (teamRepository.existsByName(request.getName())) {
            throw new CustomException(ErrorCode.TEAMNAME_ALREADY_EXISTS);
        }

        // 2. 이름 중복이 아닐 경우 팀 생성
        Team team = new Team(request.getName(), request.getDescription());
        teamRepository.save(team);

        // 3. 팀 멤버 출력을 위한 준비 - 팀 멤버 객체 생성
        List<User> users = teamMemberRepository.findUserByTeamId(team.getId());
        List<UserTeamResponseDto> members = new ArrayList<>();
        for(User user : users) {
            members.add(new UserTeamResponseDto(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole(), user.getCreatedAt()));
        }

        return new TeamCreateResponseDto(team.getId(), team.getName(), team.getDescription(), team.getCreatedAt(), members);
    }

    /**
     * 팀 목록 조회 Method
     */
    @Transactional(readOnly = true)
    public List<TeamReadResponseDto> findAll() {

        // 1. 팀 목록 출력을 위해 팀 전체 조회
        List<Team> teams = teamRepository.findAll();
        List<TeamReadResponseDto> dtos = new ArrayList<>();

        // 2. 팀 마다 속한 멤버 정보 출력
        for (Team team : teams) {
            List<User> users = teamMemberRepository.findUserByTeamId(team.getId());
            List<UserTeamResponseDto> members = new ArrayList<>();
            for(User user : users) {
                members.add(new UserTeamResponseDto(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole(), user.getCreatedAt()));
            }
            dtos.add(new TeamReadResponseDto(team.getId(), team.getName(), team.getDescription(), team.getCreatedAt(), members));
        }

        return dtos;
    }

    /**
     * 팀 상세 조회 Method
     */
    @Transactional(readOnly = true)
    public TeamReadResponseDto findOne(Long id) {

        // 1. 팀 존재 여부 확인
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 2. 팀에 속한 멤버 정보 출력
        List<User> users = teamMemberRepository.findUserByTeamId(team.getId());
        List<UserTeamResponseDto> members = new ArrayList<>();
        for(User user : users) {
            members.add(new UserTeamResponseDto(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole(), user.getCreatedAt()));
        }

        return new TeamReadResponseDto(team.getId(), team.getName(), team.getDescription(), team.getCreatedAt(), members);
    }

    /**
     * 팀 수정 Method
     */
    @Transactional
    public TeamUpdateResponseDto update(Long id, TeamUpdateRequestDto request) {

        // 1. 팀 존재 여부 확인
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 2. 요청 DTO에 name 값이 있을 경우 name 수정
        if (request.getName() != null) {
            team.updateName(request.getName());
        }

        // 3. 요청 DTO에 description 값이 있을 경우 description 수정
        if (request.getDescription() != null) {
            team.updateDescription(request.getDescription());
        }

        // 4. 팀에 속한 멤버 정보 출력
        List<User> users = teamMemberRepository.findUserByTeamId(team.getId());
        List<UserTeamResponseDto> members = new ArrayList<>();
        for(User user : users) {
            members.add(new UserTeamResponseDto(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole(), user.getCreatedAt()));
        }

        return new TeamUpdateResponseDto(team.getId(), team.getName(), team.getDescription(), team.getCreatedAt(), members);
    }

    /**
     * 팀 삭제 Method
     */
    @Transactional
    public void delete(Long id) {

        // 1. 팀 존재 여부 확인
        if (!teamRepository.existsById(id)) {
            throw new CustomException(ErrorCode.TEAM_NOT_FOUND);
        }

        // 2. 팀에 멤버가 남아있는지 확인
        if (teamMemberRepository.existsByTeamId(id)) {
            throw new CustomException(ErrorCode.TEAM_NOT_EMPTY);
        }

        teamMemberRepository.deleteById(id);
    }
}
