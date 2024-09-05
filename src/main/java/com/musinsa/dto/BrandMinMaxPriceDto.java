package com.musinsa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandMinMaxPriceDto {

    @JsonProperty("브랜드")
    private String brandName;
    @JsonProperty("가격")
    private long price;
}
