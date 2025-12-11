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
