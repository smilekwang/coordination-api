package com.musinsa.repository;

import com.musinsa.dto.*;

import java.util.List;

public interface ProductCustomRepository {

    List<LowestCategoryDto> findLowestProductByCategory();
    CategoryLowestPriceDto findProductByCategoryAndPrice(long categoryId, long price);

    List<BrandLowestPriceDto> findProductByBrand(long brandId);

    BrandMinMaxPriceDto findCategoryMinBrand(String categoryName);
    BrandMinMaxPriceDto  findCategoryMaxBrand(String categoryName);
}
