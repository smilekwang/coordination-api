package com.musinsa.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.musinsa.exception.ApplicationException;
import com.musinsa.exception.ErrorMessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {

        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<CaffeineCache> caches = Arrays.stream(CacheType.values())
                .map(cache -> new CaffeineCache(
                        cache.getName(),
                        Caffeine.newBuilder()
                                .expireAfterWrite(cache.getExpireDuration())
                                .maximumSize(cache.getMaximumSize())
                                .recordStats()
                                .build()
                )).collect(Collectors.toList());

        if (caches.isEmpty()) {
            throw new ApplicationException(ErrorMessageType.CACHE_CREATED_FAILED);
        }

        cacheManager.setCaches(caches);

        return cacheManager;
    }
}
