package com.example.process.controller;

import com.example.process.model.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 */
@Slf4j
@RestController
@RequestMapping("/health")
public class HealthController {

    /**
     * 健康检查
     *
     * @return 健康状态
     */
    @GetMapping
    public ApiResponse<Map<String, Object>> health() {
        log.info("健康检查");
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", System.currentTimeMillis());
        return ApiResponse.success("服务正常运行", data);
    }
} 