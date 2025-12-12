package com.example.taskflow.domain.search.controller;

import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.search.dto.response.SearchResponseDto;
import com.example.taskflow.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 통합 검색 기능을 제공하는 REST 컨트롤러입니다.
 * 검색어(query)를 기반으로 Task, Team, User 등 여러 도메인을 조회하며, 비즈니스 로직은 SearchService에 위임합니다.
 * 검색 결과는 SearchResponseDto로 구성하여 GlobalResponse 형태로 반환합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<GlobalResponse<SearchResponseDto>> search(@RequestParam("query") String query){
        SearchResponseDto result = searchService.search(query);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "검색 성공.", result));
    }
}
