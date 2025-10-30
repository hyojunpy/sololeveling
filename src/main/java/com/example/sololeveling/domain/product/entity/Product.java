package com.example.sololeveling.domain.product.entity;

import com.example.sololeveling.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "product",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_product_name_ci", columnNames = "name")
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_0900_ai_ci")
    private String name;

    // ENUM 타입
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private ProductType type;

    @Column(name = "interest_rate", nullable = false, precision = 10, scale = 4)
    private BigDecimal interestRate;

    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;

    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;


    public Product(String name, ProductType type, BigDecimal interestRate, Integer durationMonths, String description) {
        this.name = name;
        this.type = type;
        this.interestRate = interestRate;
        this.durationMonths = durationMonths;
        this.description = description;
    }
}
