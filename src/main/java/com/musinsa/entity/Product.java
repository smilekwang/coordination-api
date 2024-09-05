package com.musinsa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Setter
    private String name;

    @Setter
    private long price;

    @Setter
    @Column(name = "use_yn", unique = true)
    private Boolean useYn;

    @Builder
    public Product(Long id, String name, boolean useYn, Brand brand, Category category) {
        this.id = id;
        this.name = name;
        this.useYn = useYn;
        this.brand = brand;
        this.category = category;
    }
}
