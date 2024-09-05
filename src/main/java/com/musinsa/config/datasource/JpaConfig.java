package com.musinsa.config.datasource;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.musinsa.repository")
public class JpaConfig {
}
