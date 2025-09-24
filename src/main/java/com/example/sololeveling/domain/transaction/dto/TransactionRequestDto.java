package com.example.sololeveling.domain.transaction.dto;

import com.example.sololeveling.domain.transaction.entity.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TransactionRequestDto {
    private String category;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
}
