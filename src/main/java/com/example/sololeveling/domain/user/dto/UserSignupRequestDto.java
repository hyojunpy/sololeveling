package com.example.sololeveling.domain.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserSignupRequestDto {

    @Email
    @NotBlank(message = "이메일은 필수 값입니다.")
    private String email;

    @Size(max = 100)
    @NotBlank(message = "이름은 필수 값입니다.")
    private String name;

    @Size(min = 8, max = 255)
    @NotBlank(message = "비밀번호는 필수 값입니다")
    private String password;

    @Size(max = 50)
    @Pattern(regexp = "^[0-9+\\-()\\s]*$", message = "전화번호 형식이 옳지 않습니다.")
    @NotBlank(message = "연락처는 필수 값입니다.")
    private String phoneNumber;
}
