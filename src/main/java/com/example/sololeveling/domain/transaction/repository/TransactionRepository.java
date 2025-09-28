package com.example.sololeveling.domain.transaction.repository;

import com.example.sololeveling.domain.transaction.entity.Transaction;
import com.example.sololeveling.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findAllByUser(Pageable pageable, User user);

    Optional<Transaction> findByIdAndUser(Long id, User user);

    void deleteByIdAndUser(Long id, User user);
}
