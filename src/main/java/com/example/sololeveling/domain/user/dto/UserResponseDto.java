package com.example.sololeveling.domain.user.dto;

import com.example.sololeveling.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// ... existing code ...
public class UserResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private final Long id;
        private final String email;
        private final String name;
        private final String phoneNumber;

        public static Response from(User user) {
            return Response.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .phoneNumber(user.getPhoneNumber())
                    .build();
        }
    }
}
// ... existing code ...