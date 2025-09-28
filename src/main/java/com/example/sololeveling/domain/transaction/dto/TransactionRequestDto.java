package com.example.sololeveling.domain.transaction.dto;

import com.example.sololeveling.domain.transaction.entity.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TransactionRequestDto {
    @NotBlank(message = "카테고리는 필수입니다")
    @Size(max = 100, message = "카테고리는 100자를 초과할 수 없습니다")
    private String category;

    @NotNull(message = "거래 유형은 필수입니다")
    private TransactionType type;

    @NotNull(message = "금액은 필수입니다")
    @DecimalMin(value = "0.00", inclusive = false, message = "금액은 0보다 커야 합니다")
    @Digits(integer = 17, fraction = 2, message = "금액 형식이 올바르지 않습니다")
    private BigDecimal amount;

    @NotNull(message = "날짜는 필수입니다")
    private LocalDate date;

    @NotBlank(message = "설명은 필수입니다")
    private String description;
}
