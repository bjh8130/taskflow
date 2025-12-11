package com.example.taskflow.search.controller;

import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.search.dto.response.SearchResponseDto;
import com.example.taskflow.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
/*
검색어(query)1개로 Task,Team,User 각각 검색
->하나의 응답으로

검색기능 아키텍쳐 흐름
SearchController -> SearchService -> 각 도메인 Repository -> 검색 결과 DTO -> GlobalResponse

SearchController
- query 파라미터 검증
    - query null ? -> 400
    - query 공백 ? -> 400
- searchService 호출

SearchService
- 검색을 할 각 도메인의 레포지토리 호출
- 각 검색 결과 엔티티 리스트를 DTO로 변환

SearchResponseDTO

 */