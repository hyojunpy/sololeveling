package com.example.sololeveling.domain.user.repository;

import com.example.sololeveling.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(@NotBlank(message = "이메일은 필수 값입니다.") String email);
}