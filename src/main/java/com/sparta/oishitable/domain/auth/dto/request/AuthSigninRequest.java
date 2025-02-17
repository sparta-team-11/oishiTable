package com.sparta.oishitable.domain.auth.dto.request;

import com.sparta.oishitable.domain.user.entity.User;
import com.sparta.oishitable.domain.user.entity.UserRole;
import jakarta.validation.constraints.*;

public record AuthSigninRequest(
        @NotBlank(message = "이메일 입력은 필수입니다.")
        @Email(message = "올바른 이메일 형식을 입력해주세요.")
        String email,

        @NotBlank(message = "비밀번호 입력은 필수입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이내여야 합니다.")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
                message = "비밀번호는 대소문자, 숫자, 특수문자를 최소 1개씩 포함해야 합니다."
        )
        String password,

        @NotBlank(message = "이름 입력은 필수입니다.")
        String name,

        @NotBlank(message = "전화번호 입력은 필수입니다.")
        String phoneNumber,

        @NotNull
        UserRole userRole
) {

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .name(name)
                .phoneNumber(phoneNumber)
                .role(userRole)
                .build();
    }
}