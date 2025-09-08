package com.example.sololeveling.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {

    // 검증 그룹
    public interface Create {}
    public interface Update {}

    // Create: 필수, Update: 수정 금지(전송하면 검증 실패)
    @Email
    @Size(max = 320)
    @NotBlank(groups = Create.class)
    @Null(groups = Update.class)
    private String email;

    // Create/Update 공통 필수
    @NotBlank(groups = {Create.class, Update.class})
    @Size(max = 100)
    private String name;

    // Create/Update 공통 필수
    @NotBlank(groups = {Create.class, Update.class})
    @Size(min = 8, max = 255)
    private String password;

    // Create/Update 공통 필수
    @NotBlank(groups = {Create.class, Update.class})
    @Size(max = 50)
    @Pattern(regexp = "^[0-9+\\-()\\s]*$", message = "전화번호 형식이 올바르지 않습니다.", groups = {Create.class, Update.class})
    private String phoneNumber;
}