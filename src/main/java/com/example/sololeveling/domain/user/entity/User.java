package com.example.sololeveling.domain.user.entity;

import com.example.sololeveling.domain.asset.entity.Asset;
import com.example.sololeveling.domain.goal.entity.Goal;
import com.example.sololeveling.domain.recommendation.entity.Recommendation;
import com.example.sololeveling.domain.transaction.entity.Transaction;
import com.example.sololeveling.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true)
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 320, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "phone_number", nullable = false, length = 50)
    private String phoneNumber;

    // 양방향 매핑 (필수 아님) - 컬렉션 초기화
    @OneToMany(mappedBy = "user", orphanRemoval = false)
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = false)
    private List<Asset> assets = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = false)
    private List<Goal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = false)
    private List<Recommendation> recommendations = new ArrayList<>();
}
