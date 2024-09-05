package com.musinsa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "use_yn", unique = true)
    private Boolean useYn;

    @Builder
    public Category(Long id, String name, boolean useYn) {
        this.id = id;
        this.name = name;
        this.useYn = useYn;
    }
}
