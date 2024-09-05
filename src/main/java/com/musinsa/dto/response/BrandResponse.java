package com.musinsa.dto.response;

import com.musinsa.entity.BaseEntity;
import com.musinsa.entity.Brand;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BrandResponse extends BaseEntity {

    private Long id;
    private String name;
    private boolean useYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BrandResponse of(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .useYn(brand.isUseYn())
                .createdAt(brand.getCreatedAt())
                .updatedAt(brand.getUpdatedAt())
                .build();

    }
}
