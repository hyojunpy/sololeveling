package com.example.sololeveling.domain.transaction.service;

import com.example.sololeveling.domain.transaction.dto.TransactionRequestDto;
import com.example.sololeveling.domain.transaction.dto.TransactionResponseDto;
import com.example.sololeveling.domain.transaction.entity.Transaction;
import com.example.sololeveling.domain.transaction.repository.TransactionRepository;
import com.example.sololeveling.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;


    @Transactional(readOnly = true)
    public Page<Transaction> findAll(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    //Id로 유저 조회
    @Transactional(readOnly = true)
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    @Transactional
    public TransactionResponseDto createTransaction(User user,
                                                    TransactionRequestDto requestDto) {
        Transaction transaction = new Transaction(
                requestDto.getCategory(),
                requestDto.getType(),
                requestDto.getAmount(),
                requestDto.getDate(),
                requestDto.getDescription(), user);

        return TransactionResponseDto.from(transactionRepository.save(transaction));
    }


    public Optional<Transaction> updateTransaction(Long id, User user, TransactionRequestDto requestDto) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this transaction.");
        }

        return transactionRepository.findById(id).map(existing -> {
            existing.update(
                    requestDto.getCategory(),
                    requestDto.getType(),
                    requestDto.getAmount(),
                    requestDto.getDescription());
            return transactionRepository.save(existing);
        });
    }

    public void delete(Long Id, User user) {
        Optional<Transaction> transaction = transactionRepository.findById(Id);
        if (transaction.isPresent()) {
            Transaction t = transaction.get();
            if (t.getUser().getId().equals(user.getId())) {
                transactionRepository.deleteById(Id);
            }
        }
    }
}
