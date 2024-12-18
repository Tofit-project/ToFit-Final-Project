package com.tofit.mvc.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan(basePackages = "com.tofit.mvc.model.dao")
@Configuration
public class MybatisConfig {

}
