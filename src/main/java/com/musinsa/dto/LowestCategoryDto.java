package com.musinsa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LowestCategoryDto {
    private long categoryId;
    private long lowestPrice;
}
