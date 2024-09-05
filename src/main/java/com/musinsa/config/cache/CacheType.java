package com.musinsa.config.cache;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Getter
public enum CacheType {

    LOWEST_PRICE_BY_CATEGORY(
            Key.LOWEST_PRICE_BY_CATEGORY,
            Duration.ofMinutes(10),
            Duration.ofSeconds(10),
            1000
    ),
    LOWEST_BRAND_AND_CATEGORY(
            Key.LOWEST_BRAND_AND_CATEGORY,
            Duration.ofMinutes(10),
            Duration.ofSeconds(10),
            1000
    ),
    LOWEST_PRICE_AND_HIGHEST_PRICE_BY_CATEGORY(
            Key.LOWEST_PRICE_AND_HIGHEST_PRICE_BY_CATEGORY,
            Duration.ofMinutes(10),
            Duration.ofSeconds(10),
            1000
    );

    private final String name;
    private final Duration expireDuration;
    private final Duration refreshDuration;     // 현재는 사용하지 않음.
    private final int maximumSize;

    CacheType(String name, Duration expireDuration, Duration refreshDuration, int maximumSize) {
        this.name = name;
        this.expireDuration = expireDuration;
        this.refreshDuration = refreshDuration;
        this.maximumSize = maximumSize;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Key {
        public static final String LOWEST_PRICE_BY_CATEGORY = "lowest-price-by-category";
        public static final String LOWEST_BRAND_AND_CATEGORY = "lowest-brand-and-category";
        public static final String LOWEST_PRICE_AND_HIGHEST_PRICE_BY_CATEGORY = "lowest-price-highest-price-by-category";
    }
}
