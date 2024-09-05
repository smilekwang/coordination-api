package com.musinsa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;

    @Setter
    @Column(name = "use_yn", unique = true)
    private boolean useYn;

    @Builder
    public Brand(Long id, String name, boolean useYn) {
        this.id = id;
        this.name = name;
        this.useYn = useYn;
    }

}
