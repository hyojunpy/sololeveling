package com.example.sololeveling.domain.user.service;

import com.example.sololeveling.domain.user.dto.UserLoginRequestDto;
import com.example.sololeveling.domain.user.dto.UserResponseDto;
import com.example.sololeveling.domain.user.dto.UserSignupRequestDto;
import com.example.sololeveling.domain.user.dto.UserUpdateRequestDto;
import com.example.sololeveling.domain.user.entity.Role;
import com.example.sololeveling.domain.user.entity.User;
import com.example.sololeveling.domain.user.entity.UserStatus;
import com.example.sololeveling.domain.user.repository.UserRepository;
import com.example.sololeveling.global.config.dto.JwtAuthResponse;
import com.example.sololeveling.global.util.AuthenticationScheme;
import com.example.sololeveling.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;


    //전체 유저 조회
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    //Id로 유저 조회
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    //유저 생성(회원가입)
    @Transactional
    public UserResponseDto create(UserSignupRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(requestDto.getPassword());

        User user = new User(
                requestDto.getEmail(),
                requestDto.getName(),
                encodedPassword,
                requestDto.getPhoneNumber(),
                'N',   //삭제 여부
                UserStatus.Activate,
                Role.ROLE_USER
        );

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        return UserResponseDto.from(user);
    }

    //로그인
    @Transactional
    public JwtAuthResponse login(UserLoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("NOT_FOUND_VALUE"));

        if (user.getDelYN() == 'Y' || user.getStatus() != UserStatus.Activate) {
            throw new IllegalStateException("비활성화된 사용자입니다.");
        }

        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, String> generatedTokens = jwtProvider.generateTokens(user.getEmail(), user.getRole());
        String accessToken = generatedTokens.get("access_token");
        String refreshToken = generatedTokens.get("refresh_token");

        return new JwtAuthResponse(AuthenticationScheme.BEARER.getName(), accessToken, refreshToken);
    }

    //유저 정보 수정
    @Transactional
    public Optional<User> update(Long id, UserUpdateRequestDto requestDto) {

        return userRepository.findById(id).map(existing -> {
            final String updatedPassword =
                    (requestDto.getPassword() != null && !requestDto.getPassword().isBlank())
                            ? bCryptPasswordEncoder.encode(requestDto.getPassword())
                            : existing.getPassword();
            existing.update(
                    requestDto.getName(),
                    updatedPassword,
                    requestDto.getPhoneNumber(),
                    existing.getStatus()
            );
            return userRepository.save(existing);
        });
    }

    //유저 삭제(소프트 del)
    @Transactional
    public void delete(String Email, Long id) {
        if (userRepository.existsByEmail(Email)) {
            userRepository.findByEmail(Email).ifPresent(user -> user.softDelete());
        }
    }
}