package com.example.sololeveling.domain.user.service;

import com.example.sololeveling.domain.user.controller.UserController;
import com.example.sololeveling.domain.user.entity.User;
import com.example.sololeveling.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

// ... existing code ...
@Service
@RequiredArgsConstructor
public class UserService implements UserController.UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User create(String email, String name, String rawPassword, String phoneNumber) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        User user = new User(
                null,
                email,
                name,
                rawPassword,
                phoneNumber,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> update(Long id, String name, String rawPassword, String phoneNumber) {
        return userRepository.findById(id).map(existing -> {
            User updated = new User(
                    existing.getId(),
                    existing.getEmail(), // 이메일은 변경하지 않음
                    name,
                    rawPassword,
                    phoneNumber,
                    existing.getTransactions(),
                    existing.getAssets(),
                    existing.getGoals(),
                    existing.getRecommendations()
            );
            return userRepository.save(updated);
        });
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }
}
// ... existing code ...