package com.example.sololeveling.domain.user.controller;

import com.example.sololeveling.domain.user.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

// User 엔티티는 이미 존재한다고 가정합니다.
// 서비스/리포지토리 계층은 프로젝트 상황에 맞춰 주입받아 사용하세요.
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService; // 프로젝트에 맞는 구현체(예: @Service)를 제공하세요.

    // 사용자 목록 조회 (페이징)
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(@PageableDefault(size = 20) Pageable pageable) {
        Page<UserResponse> page = userService.findAll(pageable).map(UserResponse::from);
        return ResponseEntity.ok(page);
    }

    // 단일 사용자 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(UserResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 사용자 생성
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        var created = userService.create(request.email(), request.name(), request.password(), request.phoneNumber());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(UserResponse.from(created));
    }

    // 사용자 수정(전체 업데이트 의미의 PUT)
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        return userService.update(
                        id,
                        request.name(),
                        request.password(),
                        request.phoneNumber()
                )
                .map(UserResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 사용자 삭제
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

    // 응답 DTO (비밀번호 미노출)
    public static record UserResponse(
            Long id,
            String email,
            String name,
            String phoneNumber
    ) {
        public static UserResponse from(User user) {
            return new UserResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getPhoneNumber()
            );
        }
    }

    // 생성 요청 DTO
    public static record CreateUserRequest(
            @NotBlank
            @Email
            @Size(max = 320)
            String email,

            @NotBlank
            @Size(max = 100)
            String name,

            @NotBlank
            @Size(min = 8, max = 255)
            String password,

            @NotBlank
            @Size(max = 50)
            @Pattern(regexp = "^[0-9+\\-()\\s]*$", message = "전화번호 형식이 올바르지 않습니다.")
            String phoneNumber
    ) { }

    // 수정 요청 DTO
    public static record UpdateUserRequest(
            @NotBlank
            @Size(max = 100)
            String name,

            @NotBlank
            @Size(min = 8, max = 255)
            String password,

            @NotBlank
            @Size(max = 50)
            @Pattern(regexp = "^[0-9+\\-()\\s]*$", message = "전화번호 형식이 올바르지 않습니다.")
            String phoneNumber
    ) { }

    // 서비스 계약(프로젝트에 맞는 구현체 제공 필요)
    public interface UserService {
        Page<User> findAll(Pageable pageable);
        java.util.Optional<User> findById(Long id);
        User create(String email, String name, String rawPassword, String phoneNumber);
        java.util.Optional<User> update(Long id, String name, String rawPassword, String phoneNumber);
        void delete(Long id);
    }
}