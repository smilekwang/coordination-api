package com.musinsa.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductRequest {

    @NotNull(message = "브랜드ID를 입력 해주세요.")
    private Long brandId;
    @NotNull(message = "카테고리ID를 입력 해주세요.")
    private Long categoryId;
    @NotBlank(message = "상품명을 입력 해주세요.")
    @Size(min = 1, max = 100)
    private String name;
    @NotNull(message = "true or false")
    private boolean useYn;
}
