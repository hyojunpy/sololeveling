package com.example.sololeveling.domain.goal.controller;

import com.example.sololeveling.domain.goal.dto.GoalRequestDto;
import com.example.sololeveling.domain.goal.dto.GoalResponseDto;
import com.example.sololeveling.domain.goal.service.GoalService;
import com.example.sololeveling.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;

    @PostMapping("/find")
    public ResponseEntity<Page<GoalResponseDto>> getTransactions(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<GoalResponseDto> page = goalService.findAllByUser(userDetails.getUser(), pageable).map(GoalResponseDto::from);
        return ResponseEntity.ok(page);
    }

    //거래 내역 조회(단건)
    @PostMapping("/find/{id}")
    public ResponseEntity<GoalResponseDto> getTransaction(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return goalService.findByIdForUser(id, userDetails.getUser())
                .map(GoalResponseDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //목표 생성
    @PostMapping
    public ResponseEntity<GoalResponseDto> createGoal(@RequestBody GoalRequestDto requestDto,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        GoalResponseDto responseDto = goalService.createGoal(userDetails.getUser() ,requestDto);
        URI location = URI.create("/api/goals/" + responseDto.getId());

        return ResponseEntity.created(location).body(responseDto);
    }

    //목표 수정
    @PostMapping("edit/{id}")
    public ResponseEntity<GoalResponseDto> updateGoal(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody GoalRequestDto requestDto) {

        return goalService.updateGoal(id, userDetails.getUser(), requestDto)
                .map(GoalResponseDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/remove/{id}")
    public void deleteGoal(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        goalService.delete(id, userDetails.getUser());
    }
}
