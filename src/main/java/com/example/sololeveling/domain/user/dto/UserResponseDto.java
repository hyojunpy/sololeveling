package com.example.sololeveling.domain.user.dto;

import com.example.sololeveling.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String email;

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getEmail()
        );
    }
}
