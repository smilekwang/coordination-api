package com.musinsa.dto.response;

import com.musinsa.dto.CategoryLowestPriceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CategoryLowestPriceResponse {
    private List<CategoryLowestPriceDto> products;
    private long totalPrice;
}
