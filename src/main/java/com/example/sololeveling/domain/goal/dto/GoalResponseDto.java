package com.example.sololeveling.domain.goal.dto;


import com.example.sololeveling.domain.goal.entity.Goal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class GoalResponseDto {
    private final Long id;
    private final BigDecimal currentAmount;
    private final BigDecimal targetAmount;
    private final LocalDate deadline;

    public static GoalResponseDto from(Goal goal) {
        return new GoalResponseDto(
                goal.getId(),
                goal.getCurrentAmount(),
                goal.getTargetAmount(),
                goal.getDeadline()
        );
    }
}
