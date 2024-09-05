package com.musinsa.repository;

import com.musinsa.dto.LowestBrandDto;
import com.musinsa.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select " +
            "           brand_id as brandId, brand_name as brandName, sum(a.minPrice) as totalPrice " +
            "       from (\n" +
            "            select \n" +
            "            p.brand_id, p.category_id, b.name as brand_name, min(p.price) as minPrice\n" +
            "            from product p\n" +
            "            inner join brand b on p.brand_id = b.id\n" +
            "            group by p.brand_id, p. category_id\n" +
            "            ) a\n" +
            "group by a.brand_id\n" +
            "order by sum(a.minPrice) asc\n" +
            "limit 1", nativeQuery = true)
    Optional<LowestBrandDto> findLowestBrand();
}
