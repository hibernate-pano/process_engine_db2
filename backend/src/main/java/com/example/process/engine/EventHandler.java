package com.example.process.engine;

import com.example.process.model.engine.dto.FlowEventDTO;

/**
 * 事件处理器接口
 * 用于处理流程中的各类事件
 */
public interface EventHandler {

    /**
     * 处理事件
     *
     * @param event 事件
     * @return 处理结果
     */
    boolean handleEvent(FlowEventDTO event);

    /**
     * 获取支持的事件类型
     *
     * @return 事件类型
     */
    String getSupportedEventType();
} 