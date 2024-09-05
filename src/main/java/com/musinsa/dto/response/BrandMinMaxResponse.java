package com.musinsa.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.musinsa.dto.BrandMinMaxPriceDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BrandMinMaxResponse {
    @JsonProperty("카테고리")
    private String categoryName;

    @JsonProperty("최저가")
    private BrandMinMaxPriceDto brandMinPrice;

    @JsonProperty("최고가")
    private BrandMinMaxPriceDto brandMaxPrice;
}
