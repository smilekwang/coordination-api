package com.musinsa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BrandLowestPriceDto {
    @JsonProperty("카테고리")
    private String categoryName;
    @JsonProperty("가격")
    private long price;
}
