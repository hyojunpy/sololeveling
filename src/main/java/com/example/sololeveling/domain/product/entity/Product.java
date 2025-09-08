package com.example.sololeveling.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ENUM 타입
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private ProductType type;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "interest_rate", nullable = false, precision = 10, scale = 4)
    private BigDecimal interestRate;

    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;

    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
}
