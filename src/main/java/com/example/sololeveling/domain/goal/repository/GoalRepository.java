package com.example.sololeveling.domain.goal.repository;

import com.example.sololeveling.domain.goal.entity.Goal;
import com.example.sololeveling.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    Page<Goal> findAllByUser(User user, Pageable pageable);

    Optional<Goal> findByIdAndUser(Long id, User user);
}
