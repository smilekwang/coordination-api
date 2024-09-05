package com.musinsa.dto.response;

import com.musinsa.entity.BaseEntity;
import com.musinsa.entity.Product;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProductResponse extends BaseEntity {

    private Long id;
    private Long categoryId;
    private Long brandId;
    private String name;
    private boolean useYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .categoryId(product.getCategory().getId())
                .brandId(product.getBrand().getId())
                .name(product.getName())
                .useYn(product.getUseYn())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();

    }
}
