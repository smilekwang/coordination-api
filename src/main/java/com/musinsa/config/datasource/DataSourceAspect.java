package com.musinsa.config.datasource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
public class DataSourceAspect {

    @Before("@annotation(org.springframework.transaction.annotation.Transactional) && @annotation(transactional)")
    public void setDataSourceType(Transactional transactional) {
        if (transactional.readOnly()) {
            DataSourceContextHolder.setDataSourceType(DataSourceType.SLAVE);
        } else {
            DataSourceContextHolder.setDataSourceType(DataSourceType.MASTER);
        }
    }
}
