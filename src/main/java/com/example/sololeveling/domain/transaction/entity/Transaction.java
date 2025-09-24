package com.example.sololeveling.domain.transaction.entity;

import com.example.sololeveling.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", nullable = false, length = 100)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10)
    private TransactionType type;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    // FK: user_id -> users.id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Transaction(String category, TransactionType type, BigDecimal amount, LocalDate date, String description, User user) {
        this.category = category;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.user = user;
    }

    public void update(String category, TransactionType type, BigDecimal amount, String description) {
        this.category = category;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }
}
