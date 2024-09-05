package com.musinsa.controller.admin;

import com.musinsa.dto.request.BrandRequest;
import com.musinsa.dto.request.ProductRequest;
import com.musinsa.dto.response.BrandResponse;
import com.musinsa.dto.response.ProductResponse;
import com.musinsa.service.admin.BrandService;
import com.musinsa.service.customer.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final BrandService brandService;
    private final ProductService productService;

    @PostMapping("/brand")
    @Operation(summary = "브랜드 등록", description = "신규 브랜드를 등록 합니다.")
    public ResponseEntity<BrandResponse> createBrand(@Valid @RequestBody BrandRequest brandRequest) {
        BrandResponse newBrand = brandService.createBrand(brandRequest);
        return ResponseEntity.ok(newBrand);
    }

    @PutMapping("/brand/{brandId}")
    @Operation(summary = "브랜드 업데이트", description = "브랜드를 수정 합니다.")
    public ResponseEntity<BrandResponse> updateBrand(
            @Valid @RequestBody BrandRequest brandRequest,
            @PathVariable("brandId") Long brandId) {
        BrandResponse updateBrand = brandService.updateBrand(brandId, brandRequest);
        return ResponseEntity.ok(updateBrand);
    }

    @DeleteMapping("/brand/{brandId}")
    @Operation(summary = "브랜드 삭제", description = "삭제는 soft delete 합니다.")
    public ResponseEntity<BrandResponse> deleteBrand(
            @PathVariable("brandId") Long brandId) {
        BrandResponse updateBrand = brandService.deleteBrand(brandId);
        return ResponseEntity.ok(updateBrand);
    }

    @PostMapping("/product")
    @Operation(summary = "상품 등록", description = "신규 상품을 등록 합니다.")
    public ResponseEntity<ProductResponse> createBrand(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse newProduct = productService.createProduct(productRequest);
        return ResponseEntity.ok(newProduct);
    }

    @PutMapping("/product/{productId}")
    @Operation(summary = "상품 업데이트", description = "상품을 수정 합니다.")
    public ResponseEntity<ProductResponse> updateProduct(
            @Valid @RequestBody ProductRequest productRequest,
            @PathVariable("productId") Long productId) {
        ProductResponse updateProduct = productService.updateProduct(productId, productRequest);
        return ResponseEntity.ok(updateProduct);
    }

    @DeleteMapping("/product/{productId}")
    @Operation(summary = "상품 삭제", description = "삭제는 soft delete 합니다.")
    public ResponseEntity<ProductResponse> deleteProduct(
            @PathVariable("productId") Long productId) {
        ProductResponse updateProduct = productService.deleteProduct(productId);
        return ResponseEntity.ok(updateProduct);
    }
}
