package com.musinsa.service.customer;

import com.musinsa.dto.*;
import com.musinsa.dto.request.ProductRequest;
import com.musinsa.dto.response.*;
import com.musinsa.entity.Brand;
import com.musinsa.entity.Category;
import com.musinsa.entity.Product;
import com.musinsa.exception.ApplicationException;
import com.musinsa.exception.ErrorMessageType;
import com.musinsa.repository.BrandRepository;
import com.musinsa.repository.CategoryRepository;
import com.musinsa.repository.ProductCustomRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.musinsa.repository.ProductRepository;
import java.util.*;
import java.util.stream.Collectors;

import static com.musinsa.config.cache.CacheType.Key.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCustomRepositoryImpl productCustomRepository;

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = LOWEST_PRICE_BY_CATEGORY, cacheManager = "cacheManager")
    public CategoryLowestPriceResponse getLowestPriceByCategory() {

        /*
        NOTE: 상품수가 많아질수 있으므로, join을 이용하여 product를 여러번 스캔하는 경우 장기적으론 성능 저하가 예상 되므로
        1. 카테고리별 최저가를 구하고
        2. 1의 값을 가지고 카테고리수 만큼만 상품을 조회하는게 효율적이라고 판단 하였음.
        */
        List<CategoryLowestPriceDto> categoryBrandProducts = productCustomRepository.findLowestProductByCategory().stream()
                .map(lowestProduct -> productCustomRepository.findProductByCategoryAndPrice(lowestProduct.getCategoryId(), lowestProduct.getLowestPrice()))
                .collect(Collectors.toList());

        long total = categoryBrandProducts.stream()
                .mapToLong(CategoryLowestPriceDto::getPrice)
                .sum();

        return CategoryLowestPriceResponse.builder()
                .products(categoryBrandProducts)
                .totalPrice(total)
                .build();
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = LOWEST_BRAND_AND_CATEGORY, cacheManager = "cacheManager")
    public BrandLowestPriceResponse getLowestPriceByBrand() {

        /*
        NOTE: 예제에는 카테고리, 브랜드당 1개의 상품만 존재 하지만, 상품이 카테고리, 브랜드당 여러개인 경우를 고려
        1. 카테고리 브랜드별 최저 가격을 구한 후 최저가격인 브랜드의 합계를 추출한다.
        2. 해당 브랜드id를 이용하여 최저 가격 상품을 불러온다. 이때도 중복을 고려하여 최저가격을 추출해야 한다.
        */
        LowestBrandDto lowestBrand = productRepository.findLowestBrand().orElseThrow(() -> new ApplicationException(ErrorMessageType.PRODUCT_NO_REFUND));
        List<BrandLowestPriceDto> brandLowestPrices = productCustomRepository.findProductByBrand(lowestBrand.getBrandId());

        return BrandLowestPriceResponse.builder()
                .brandLowestPrice(
                        BrandLowestPriceResponse.BrandLowestPrice.builder().
                                brandName(lowestBrand.getBrandName())
                                .products(brandLowestPrices)
                                .totalPrice(lowestBrand.getTotalPrice())
                                .build()
                ).build();
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = LOWEST_PRICE_AND_HIGHEST_PRICE_BY_CATEGORY, cacheManager = "cacheManager")
    public BrandMinMaxResponse getCategoryMinMaxBrand(String categoryName) {

        /*
        NOTE: 최고/최저가 동일금액이 있는 경우 우선순위는 barand.id 정렬역순 입니다.
        */
        BrandMinMaxPriceDto minBrand = Optional.ofNullable(productCustomRepository.findCategoryMinBrand(categoryName))
                .orElseThrow(() -> new ApplicationException(ErrorMessageType.CATEGORY_PRODUCT_NO_REFUND));
        BrandMinMaxPriceDto maxBrand = Optional.ofNullable(productCustomRepository.findCategoryMaxBrand(categoryName))
                .orElseThrow(() -> new ApplicationException(ErrorMessageType.CATEGORY_PRODUCT_NO_REFUND));
        return BrandMinMaxResponse.builder()
                .categoryName(categoryName)
                .brandMinPrice(
                        BrandMinMaxPriceDto.builder()
                                .brandName(minBrand.getBrandName())
                                .price(minBrand.getPrice())
                                .build()
                )
                .brandMaxPrice(
                        BrandMinMaxPriceDto.builder()
                                .brandName(maxBrand.getBrandName())
                                .price(maxBrand.getPrice())
                                .build()
                ).build();
    }

    @Transactional
    @CacheEvict(value = {LOWEST_PRICE_BY_CATEGORY, LOWEST_BRAND_AND_CATEGORY, LOWEST_PRICE_AND_HIGHEST_PRICE_BY_CATEGORY}, allEntries = true)
    public ProductResponse createProduct(ProductRequest productRequest) {

        Brand brand = brandRepository.findById(productRequest.getBrandId())
                .orElseThrow(() -> new ApplicationException(ErrorMessageType.BRAND_NOT_FOUND));

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ApplicationException(ErrorMessageType.CATEGORY_NOT_FOUND));

        try {
            Product product = Product.builder()
                    .name(productRequest.getName())
                    .useYn(productRequest.isUseYn())
                    .category(category)
                    .brand(brand)
                    .build();
            return ProductResponse.of(productRepository.save(product));
        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ErrorMessageType.DATA_UPSERT_ERROR);
        }
    }

    @Transactional
    @CacheEvict(value = {LOWEST_PRICE_BY_CATEGORY, LOWEST_BRAND_AND_CATEGORY, LOWEST_PRICE_AND_HIGHEST_PRICE_BY_CATEGORY}, allEntries = true)
    public ProductResponse updateProduct(Long productId, ProductRequest productRequest) {
        Brand brand = brandRepository.findById(productRequest.getBrandId())
                .orElseThrow(() -> new ApplicationException(ErrorMessageType.BRAND_NOT_FOUND));

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ApplicationException(ErrorMessageType.CATEGORY_NOT_FOUND));

        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ApplicationException(ErrorMessageType.PRODUCT_NOT_FOUND));

            product.setName(productRequest.getName());
            product.setUseYn(productRequest.isUseYn());
            product.setBrand(brand);
            product.setCategory(category);

            return ProductResponse.of(product);
        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ErrorMessageType.DATA_UPSERT_ERROR);
        }
    }

    @Transactional
    @CacheEvict(value = {LOWEST_PRICE_BY_CATEGORY, LOWEST_BRAND_AND_CATEGORY, LOWEST_PRICE_AND_HIGHEST_PRICE_BY_CATEGORY}, allEntries = true)
    public ProductResponse deleteProduct(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ApplicationException(ErrorMessageType.BRAND_NOT_FOUND));

            product.setUseYn(false);

            return ProductResponse.of(product);
        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ErrorMessageType.DATA_UPSERT_ERROR);
        }
    }

}
