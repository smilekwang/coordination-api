package com.musinsa.exception;

import lombok.Getter;

@Getter
public enum ErrorMessageType {

    PRODUCT_NO_REFUND("조회된 상품이 없습니다."),
    CATEGORY_PRODUCT_NO_REFUND("카테고리에 해당하는 상품 데이터가 없습니다"),
    DATA_UPSERT_ERROR("데이터 입력중 오류"),
    BRAND_NOT_FOUND("브랜드가 없습니다."),
    PRODUCT_NOT_FOUND("상품이 없습니다."),
    CATEGORY_NOT_FOUND("카테고리가 없습니다."),
    CACHE_CREATED_FAILED("CacheType 설정이 없어, 캐시가 생성되지 않았습니다."),
    ;

    private final String errorMessage;

    ErrorMessageType(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
