package com.sparta.oishitable.domain.customer.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostCreateRequest(
        @NotNull(message = "지역을 선택 해주세요.")
        Long regionId,

        @NotBlank(message = "공백을 입력할 수 없습니다.")
        @Size(max = 20, message = "최대 20자까지 입력 가능합니다.")
        String title,

        @NotBlank(message = "공백을 입력할 수 없습니다.")
        @Size(max = 300, message = "최대 300자까지 입력 가능합니다.")
        String content
) {
}
