package com.example.sololeveling.domain.asset.entity;

import com.example.sololeveling.domain.user.entity.User;
import com.example.sololeveling.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

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

    @Column(name = "del_yn", nullable = false, length = 1)
    private Character delYN;

    // FK: user_id -> users.id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Asset(String type, String name, BigDecimal amount, Character delYN, User user) {
        this.type = type;
        this.name = name;
        this.amount = amount;
        this.delYN = delYN;
        this.user = user;
    }

    public void update(String type, String name, BigDecimal amount) {
        this.type = type;
        this.name = name;
        this.amount = amount;
    }

    @PrePersist
    void prePersist() {
        if (this.delYN == null) this.delYN = 'N';
    }
    public void softDelete() { this.delYN = 'Y'; }
}
