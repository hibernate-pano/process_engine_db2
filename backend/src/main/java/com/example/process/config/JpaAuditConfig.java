package com.example.process.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * JPA审计配置类
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {

    /**
     * 配置审计人员提供者
     * 
     * @return 审计人员提供者
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        // 这里可以从Spring Security上下文中获取当前用户
        // 暂时使用系统用户
        return () -> Optional.of("System");
    }
} 