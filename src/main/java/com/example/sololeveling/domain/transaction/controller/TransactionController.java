package com.example.sololeveling.domain.transaction.controller;


import com.example.sololeveling.domain.transaction.dto.TransactionRequestDto;
import com.example.sololeveling.domain.transaction.dto.TransactionResponseDto;
import com.example.sololeveling.domain.transaction.service.TransactionService;
import com.example.sololeveling.domain.user.entity.User;
import com.example.sololeveling.global.config.auth.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@Validated
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    //거래 내역 조회
    @PostMapping("/find")
    public ResponseEntity<Page<TransactionResponseDto>> getTransactions(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<TransactionResponseDto> page = transactionService.findAllByUser(userDetails.getUser(), pageable).map(TransactionResponseDto::from);
        return ResponseEntity.ok(page);
    }

    //거래 내역 조회(단건)
    @PostMapping("/find/{id}")
    public ResponseEntity<TransactionResponseDto> getTransaction(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return transactionService.findByIdForUser(id, userDetails.getUser())
                .map(TransactionResponseDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //거래 생성
    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction
            (@AuthenticationPrincipal UserDetailsImpl userDetails,
             @Valid @RequestBody TransactionRequestDto requestDto) {
        User user = userDetails.getUser();
        TransactionResponseDto responseDto = transactionService.createTransaction(user, requestDto);
        URI location = URI.create("/api/transactions/" + responseDto.getId());

        return ResponseEntity.created(location).body(responseDto);
    }

    //거래 수정
    @PostMapping("edit/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody TransactionRequestDto requestDto
    ) {
        User user = userDetails.getUser();
        return transactionService.updateTransaction(id, user, requestDto)
                .map(TransactionResponseDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/remove/{id}")
    public void deleteTransaction(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        transactionService.delete(id, userDetails.getUser());
    }
}
