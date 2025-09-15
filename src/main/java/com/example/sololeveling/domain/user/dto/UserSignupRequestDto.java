package com.example.sololeveling.domain.user.dto;


import com.example.sololeveling.domain.user.entity.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSignupRequestDto {

    @NotBlank(message = "이메일은 필수 값입니다.")
    private final String email;

    @NotBlank(message = "이름은 필수 값입니다.")
    private final String name;

    @NotBlank(message = "비밀번호는 필수 값입니다")
    private final String password;

    @NotBlank(message = "연락처는 필수 값입니다.")
    private final String phoneNumber;
}
