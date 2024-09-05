package com.musinsa.controller.customer;

import com.musinsa.dto.response.BrandLowestPriceResponse;
import com.musinsa.dto.response.BrandMinMaxResponse;
import com.musinsa.dto.response.CategoryLowestPriceResponse;
import com.musinsa.service.customer.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer/coordination")
public class CustomerController {

    private final ProductService productService;

    @GetMapping("/category/lowest-price")
    @Operation(summary = "카테고리별 최저가", description = "카테고리별 최저 가격인 브랜드 가격과 총액을 제공 합니다.")
    public ResponseEntity<CategoryLowestPriceResponse> getLowestPriceByCategory() {

        CategoryLowestPriceResponse categoryLowestPrice = productService.getLowestPriceByCategory();
        return ResponseEntity.ok(categoryLowestPrice);
    }

    @GetMapping("/brand/lowest-price")
    @Operation(summary = "가장 저렴한 브랜드 카테고리", description = "전체 상품 카테고리 상품중 단일브랜드로 최저가격인 브랜드와 총액을 제공 합니다.")
    public ResponseEntity<BrandLowestPriceResponse> getLowestPriceByBrand() {

        BrandLowestPriceResponse brandLowestPrice = productService.getLowestPriceByBrand();
        return ResponseEntity.ok(brandLowestPrice);
    }

    @GetMapping("/category/{categoryName}/min-max-brand")
    @Operation(summary = "카테고리 최저가, 최고가 브랜드", description = "특정 카테고리에서 최저가격 브랜드와 최고가격 브랜드 정보를 제공 합니다.")
    public ResponseEntity<BrandMinMaxResponse> getCategoryMinMaxBrand(
            @PathVariable("categoryName") String categoryName) {

        BrandMinMaxResponse brandMinMaxResponse = productService.getCategoryMinMaxBrand(categoryName);
        return ResponseEntity.ok(brandMinMaxResponse);
    }
}
