package com.musinsa.service.admin;

import com.musinsa.dto.request.BrandRequest;
import com.musinsa.dto.response.BrandResponse;
import com.musinsa.entity.Brand;
import com.musinsa.exception.ApplicationException;
import com.musinsa.exception.ErrorMessageType;
import com.musinsa.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.musinsa.config.cache.CacheType.Key.*;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    @Transactional
    @CacheEvict(value = {LOWEST_PRICE_BY_CATEGORY, LOWEST_BRAND_AND_CATEGORY, LOWEST_PRICE_AND_HIGHEST_PRICE_BY_CATEGORY}, allEntries = true)
    public BrandResponse createBrand(BrandRequest brandRequest) {
        try {
            Brand brand = Brand.builder()
                    .name(brandRequest.getName())
                    .useYn(brandRequest.isUseYn())
                    .build();
            return BrandResponse.of(brandRepository.save(brand));
        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ErrorMessageType.DATA_UPSERT_ERROR);
        }
    }

    @Transactional
    @CacheEvict(value = {LOWEST_PRICE_BY_CATEGORY, LOWEST_BRAND_AND_CATEGORY, LOWEST_PRICE_AND_HIGHEST_PRICE_BY_CATEGORY}, allEntries = true)
    public BrandResponse updateBrand(Long brandId, BrandRequest brandRequest) {
        try {
            Brand brand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new ApplicationException(ErrorMessageType.BRAND_NOT_FOUND));

            brand.setName(brandRequest.getName());
            brand.setUseYn(brandRequest.isUseYn());

            return BrandResponse.of(brand);
        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ErrorMessageType.DATA_UPSERT_ERROR);
        }
    }

    @Transactional
    @CacheEvict(value = {LOWEST_PRICE_BY_CATEGORY, LOWEST_BRAND_AND_CATEGORY, LOWEST_PRICE_AND_HIGHEST_PRICE_BY_CATEGORY}, allEntries = true)
    public BrandResponse deleteBrand(Long brandId) {
        try {
            Brand brand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new ApplicationException(ErrorMessageType.BRAND_NOT_FOUND));

            brand.setUseYn(false);

            return BrandResponse.of(brand);
        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ErrorMessageType.DATA_UPSERT_ERROR);
        }
    }
}
