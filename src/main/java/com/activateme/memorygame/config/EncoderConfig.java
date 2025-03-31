package com.activateme.memorygame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// 循环依赖的解决方法通常是打破依赖链
// 一种方案是将 BCryptPasswordEncoder 移到单独的配置类
@Configuration
public class EncoderConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}