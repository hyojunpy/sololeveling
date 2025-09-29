package com.example.sololeveling.domain.transaction.dto;

import com.example.sololeveling.domain.transaction.entity.Transaction;
import com.example.sololeveling.domain.transaction.entity.TransactionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class TransactionResponseDto {
    private final Long id;
    private final String category;
    private final TransactionType type;
    private final BigDecimal amount;
    private final LocalDate date;
    private final String description;

    public static TransactionResponseDto from(Transaction transaction) {
        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getCategory(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getDescription()
        );
    }
}
