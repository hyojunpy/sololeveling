package com.example.sololeveling.domain.goal.entity;

import com.example.sololeveling.domain.user.entity.User;
import com.example.sololeveling.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "goal")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Goal extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK: user_id -> users.id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "target_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal targetAmount;

    @Column(name = "current_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal currentAmount;

    // deadline 컬럼명, DATE 타입은 LocalDate 권장
    @Column(name = "deadline", nullable = false)
    private LocalDate deadline;

    public Goal(User user, BigDecimal currentAmount, BigDecimal targetAmount, LocalDate deadline) {
        this.user = user;
        this.currentAmount = currentAmount;
        this.targetAmount = targetAmount;
        this.deadline = deadline;
    }

    public void update(BigDecimal targetAmount, BigDecimal currentAmount, LocalDate deadline) {
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
    }

}
