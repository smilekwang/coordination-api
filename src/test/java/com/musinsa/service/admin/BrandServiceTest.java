package com.musinsa.service.admin;

import com.musinsa.dto.request.BrandRequest;
import com.musinsa.dto.response.BrandResponse;
import com.musinsa.entity.Brand;
import com.musinsa.entity.Product;
import com.musinsa.repository.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBrand() {
        // Given
        BrandRequest brandRequest = BrandRequest.builder()
                .name("브랜드-1")
                .useYn(true)
                .build();
        Brand brand = new Brand(1L, "브랜드-1", true);

        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        // When
        BrandResponse response = brandService.createBrand(brandRequest);

        // Then
        verify(brandRepository, times(1)).save(any(Brand.class));
        assertEquals("브랜드-1", response.getName());
        assertEquals(true, response.isUseYn());
    }

    @Test
    void updateBrand() {
        // Given
        Long brandId = 3L;
        BrandRequest brandRequest = BrandRequest.builder()
                .name("브랜드-2")
                .useYn(false)
                .build();
        Brand existingBrand = new Brand(1L, "브랜드-1", true);
        Brand updatedBrand = new Brand(1L, "브랜드-2", false);

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.save(any(Brand.class))).thenReturn(updatedBrand);

        // When
        BrandResponse response = brandService.updateBrand(brandId, brandRequest);

        // Then
        verify(brandRepository, times(1)).findById(brandId);
        assertEquals("브랜드-2", response.getName());
        assertEquals(false, response.isUseYn());
    }

    @Test
    void deleteBrand() {
        // Given
        Long brandId = 1L;
        Brand existingBrand = new Brand(1L, "브랜드-1", true);

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(existingBrand));

        // When
        BrandResponse response = brandService.deleteBrand(brandId);

        // Then
        verify(brandRepository, times(1)).findById(brandId);
        assertEquals("브랜드-1", response.getName());
        assertEquals(false, response.isUseYn());
    }
}
