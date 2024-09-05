package com.musinsa.repository;

import com.musinsa.dto.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.musinsa.entity.QCategory.category;
import static com.musinsa.entity.QProduct.product;
import static com.musinsa.entity.QBrand.brand;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<LowestCategoryDto> findLowestProductByCategory() {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                LowestCategoryDto.class,
                                product.category.id,
                                product.price.min().as("price")
                        )
                )
                .from(product)
                .join(brand).on(product.brand.id.eq(brand.id))
                .join(category).on(product.category.id.eq(category.id))
                .where(
                        product.useYn.eq(true)
                            .and(brand.useYn.eq(true)
                            .and(category.useYn.eq(true)))
                )
                .groupBy(product.category.id)
                .fetch();
    }

    @Override
    public CategoryLowestPriceDto findProductByCategoryAndPrice(long categoryId, long price) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                CategoryLowestPriceDto.class,
                                category.name,
                                brand.name,
                                product.price
                        )
                )
                .from(product)
                .where(product.category.id.eq(categoryId).and(product.price.eq(price)))
                .orderBy(product.brand.id.desc())
                .fetchFirst();
    }

    @Override
    public List<BrandLowestPriceDto> findProductByBrand(long brandId) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                BrandLowestPriceDto.class,
                                product.category.name,
                                product.price.min().as("price")
                        )
                )
                .from(product)
                .join(category).on(product.category.id.eq(category.id))
                .where(
                        product.useYn.eq(true)
                                .and(brand.useYn.eq(true)
                                        .and(category.useYn.eq(true)))
                                .and(product.brand.id.eq(brandId))
                )
                .groupBy(product.category.id)
                .fetch();
    }

    @Override
    public BrandMinMaxPriceDto findCategoryMinBrand(String categoryName) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                BrandMinMaxPriceDto.class,
                                brand.name.as("brandName"),
                                product.price
                        )
                )
                .from(product)
                .join(category).on(product.category.id.eq(category.id))
                .join(brand).on(product.brand.id.eq(brand.id))
                .where(product.price.eq(
                        JPAExpressions
                                .select(product.price.min())
                                .from(product)
                                .where(product.category.name.eq(categoryName))
                        )
                )
                .orderBy(product.brand.id.desc())
                .fetchFirst();
    }

    @Override
    public BrandMinMaxPriceDto findCategoryMaxBrand(String categoryName) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                BrandMinMaxPriceDto.class,
                                brand.name.as("brandName"),
                                product.price
                        )
                )
                .from(product)
                .join(category).on(product.category.id.eq(category.id))
                .join(brand).on(product.brand.id.eq(brand.id))
                .where(product.price.eq(
                                JPAExpressions
                                        .select(product.price.max())
                                        .from(product)
                                        .where(product.category.name.eq(categoryName))
                        )
                )
                .orderBy(product.brand.id.desc())
                .fetchFirst();
    }
}
