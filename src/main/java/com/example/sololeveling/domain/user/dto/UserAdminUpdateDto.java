package com.example.sololeveling.domain.user.dto;

import com.example.sololeveling.domain.user.entity.Role;
import com.example.sololeveling.domain.user.entity.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAdminUpdateDto {
    @Email
    private String email;

    @Size(max = 100)
    private String name;

    @Size(min = 8, max = 255)
    private String password;

    @Size(max = 50)
    @Pattern(regexp = "^[0-9+\\-()\\s]*$", message = "전화번호를 형식이 옳지 않습니다.")
    private String phoneNumber;

    private UserStatus userStatus;

    private Role role;
}
