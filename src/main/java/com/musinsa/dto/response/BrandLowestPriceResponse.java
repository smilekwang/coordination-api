package com.musinsa.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.musinsa.dto.BrandLowestPriceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BrandLowestPriceResponse {

    @JsonProperty("최저가")
    private BrandLowestPrice brandLowestPrice;

    @Getter
    @Builder
    public static class BrandLowestPrice {
        @JsonProperty("카테고리")
        private List<BrandLowestPriceDto> products;
        @JsonProperty("브랜드")
        private String brandName;
        @JsonProperty("총액")
        private long totalPrice;
    }
}
