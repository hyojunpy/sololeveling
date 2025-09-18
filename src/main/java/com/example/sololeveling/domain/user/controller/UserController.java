package com.example.sololeveling.domain.user.controller;

import com.example.sololeveling.domain.user.dto.UserLoginRequestDto;
import com.example.sololeveling.domain.user.dto.UserResponseDto;
import com.example.sololeveling.domain.user.dto.UserSignupRequestDto;
import com.example.sololeveling.domain.user.dto.UserUpdateRequestDto;
import com.example.sololeveling.domain.user.service.UserService;
import com.example.sololeveling.global.config.auth.UserDetailsImpl;
import com.example.sololeveling.global.config.dto.JwtAuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    // 사용자 목록 조회 (페이징)
    @PostMapping
    public ResponseEntity<Page<UserResponseDto>> getUsers(@PageableDefault(size = 20) Pageable pageable) {
        Page<UserResponseDto> page = userService.findAll(pageable).map(UserResponseDto::from);
        return ResponseEntity.ok(page);
    }

    // 단일 사용자 조회
    @PostMapping("/find-id/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(UserResponseDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 사용자 생성(회원 가입)
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @ModelAttribute UserSignupRequestDto request) {
        UserResponseDto userResponseDto = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody UserLoginRequestDto request) {
        JwtAuthResponse authResponse =  userService.login(request);
        return ResponseEntity.ok(authResponse);
    }

    // 사용자 정보 수정
    @PostMapping("/edit-info/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDto request
    ) {
        return userService.update(id, request)
                .map(UserResponseDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 사용자 삭제
    @PostMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                           HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Authentication authentication) {
        userService.delete(userDetails.getUsername(),id);

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("회원 삭제에 실패하였습니다");
        }
        new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, null);
    }
}