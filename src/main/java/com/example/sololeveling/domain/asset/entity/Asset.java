package com.example.sololeveling.domain.asset.entity;

import com.example.sololeveling.domain.user.entity.User;
import com.example.sololeveling.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "asset")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Asset extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // VARCHAR 타입 요구사항 반영
    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    // DECIMAL 매핑
    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    // FK: user_id -> users.id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void updateAmount(BigDecimal delta) {
        this.amount = this.amount.add(delta);
    }
}
