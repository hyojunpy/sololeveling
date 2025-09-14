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

import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationMan;
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

        user = userRepository.save(user);

        return UserResponseDto.from(user);
    }

    //로그인
    @Transactional
    public JwtAuthResponse login(UserLoginRequestDto requestDto) {

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("NOT_FOUND_VALUE"));

        Authentication authentication = this.authenticationMan.authenticate(
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

    @Transactional
    public Optional<User> update(Long id ,UserUpdateRequestDto requestDto) {

        String encodedPassword = bCryptPasswordEncoder.encode(requestDto.getPassword());

        return userRepository.findById(id).map(existing -> {
            User updated = new User(
                    existing.getEmail(),
                    requestDto.getName(),
                    encodedPassword,
                    requestDto.getPhoneNumber(),
                    'Y',
                    UserStatus.Activate,
                    Role.ROLE_USER
            );
            return userRepository.save(updated);
        });
    }

    @Transactional
    public void delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }
}