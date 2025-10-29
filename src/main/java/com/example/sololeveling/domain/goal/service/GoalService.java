package com.example.sololeveling.domain.goal.service;

import com.example.sololeveling.domain.goal.dto.GoalRequestDto;
import com.example.sololeveling.domain.goal.dto.GoalResponseDto;
import com.example.sololeveling.domain.goal.entity.Goal;
import com.example.sololeveling.domain.goal.repository.GoalRepository;
import com.example.sololeveling.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository goalRepository;

    //전체 조회
    @Transactional(readOnly = true)
    public Page<Goal> findAllByUser(User user, Pageable pageable) {
        return goalRepository.findAllByUser(user, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Goal> findByIdForUser(Long id, User user) {
        return goalRepository.findByIdAndUser(id, user);
    }


    //목표 생성
    @Transactional
    public GoalResponseDto createGoal(User user, GoalRequestDto requestDto) {
        Goal goal = new Goal(
                user,
                requestDto.getCurrentAmount(),
                requestDto.getTargetAmount(),
                requestDto.getDeadline()
        );

        return GoalResponseDto.from(goalRepository.save(goal));
    }

    //목표 수정
    @Transactional
    public Optional<Goal> updateGoal(Long id, User user, GoalRequestDto requestDto) {
        Goal goal= goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No such goal."));

        if(!goal.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not have permission to modify this goal.");
        }

        goal.update(requestDto.getCurrentAmount(), requestDto.getTargetAmount(), requestDto.getDeadline());
        return Optional.of(goalRepository.save(goal));
    }

    //목표 삭제
    public void delete(Long id, User user) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No such goal."));

        if(!goal.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not have permission to modify this goal.");
        }

        goalRepository.delete(goal);
    }


}
