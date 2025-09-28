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

    //거래 전체 조회
    @Transactional(readOnly = true)
    public Page<Transaction> findAllByUser(Pageable pageable, User user) {
        return transactionRepository.findAllByUser(user, pageable);
    }

    //Id로 거래 조회
    @Transactional(readOnly = true)
    public Optional<Transaction> findByIdForUser(Long id, User user) {
        return transactionRepository.findByIdAndUser(id, user);
    }

    //거래 내역 생성
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


    //거래 내역 수정
    @Transactional
    public Optional<Transaction> updateTransaction(Long id, User user, TransactionRequestDto requestDto) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this transaction.");
        }

        transaction.update(
                requestDto.getCategory(),
                requestDto.getType(),
                requestDto.getDate(),
                requestDto.getAmount(),
                requestDto.getDescription());
        return Optional.of(transactionRepository.save(transaction));
    }

    //거래 내역 삭제
    @Transactional
    public void delete(Long id, User user) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this transaction.");
        }

        transactionRepository.deleteByIdAndUser(id, user);
    }
}
