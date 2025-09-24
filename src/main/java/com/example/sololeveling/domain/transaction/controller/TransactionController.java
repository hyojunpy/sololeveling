package com.example.sololeveling.domain.transaction.controller;


import com.example.sololeveling.domain.transaction.dto.TransactionRequestDto;
import com.example.sololeveling.domain.transaction.dto.TransactionResponseDto;
import com.example.sololeveling.domain.transaction.service.TransactionService;
import com.example.sololeveling.domain.user.entity.User;
import com.example.sololeveling.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/find")
    public ResponseEntity<Page<TransactionResponseDto>> getTransactions(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Page<TransactionResponseDto> page = transactionService.findAll(pageable).map(TransactionResponseDto::from);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/find/{id}")
    public ResponseEntity<TransactionResponseDto> getTransaction(@PathVariable Long id) {
        return transactionService.findById(id)
                .map(TransactionResponseDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction
            (@AuthenticationPrincipal UserDetailsImpl userDetails,
             @RequestBody TransactionRequestDto requestDto) {
        User user = userDetails.getUser();
        TransactionResponseDto transactionResponseDto = transactionService.createTransaction(user, requestDto);
        return ResponseEntity.ok(transactionResponseDto);
    }

    @PostMapping("edit/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody TransactionRequestDto requestDto
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
