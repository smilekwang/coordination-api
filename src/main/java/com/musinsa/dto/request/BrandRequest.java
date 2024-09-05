package com.musinsa.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BrandRequest {

    @NotBlank(message = "브랜드명을 입력해주세요.")
    @Size(min = 1, max = 100)
    private String name;
    @NotNull(message = "true or false")
    private boolean useYn;
}
