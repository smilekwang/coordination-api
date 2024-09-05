package com.musinsa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryLowestPriceDto {
    private String categoryName;
    private String brandName;
    private long price;
}
