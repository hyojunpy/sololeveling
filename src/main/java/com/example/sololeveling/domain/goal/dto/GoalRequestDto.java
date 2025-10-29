package com.example.sololeveling.domain.goal.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class GoalRequestDto {
    private final BigDecimal currentAmount;
    private final BigDecimal targetAmount;
    private final LocalDate deadline;


}
