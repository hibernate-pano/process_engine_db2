package com.example.process.engine.impl;

import com.example.process.engine.EventHandler;
import com.example.process.model.engine.dto.FlowEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 默认事件处理器实现
 * 处理基本事件类型
 */
@Component
@Slf4j
public class DefaultEventHandler implements EventHandler {

    private static final String EVENT_TYPE = "default";

    @Override
    public boolean handleEvent(FlowEventDTO event) {
        log.info("处理默认事件: {}, 事件ID: {}", event.getEventType(), event.getId());
        
        // 默认事件处理器只是记录事件信息
        log.info("事件名称: {}", event.getEventName());
        log.info("事件源: {}", event.getSourceId());
        log.info("事件源类型: {}", event.getSourceType());
        log.info("事件数据: {}", event.getEventData());
        
        // 实际应用中，这里应该根据事件类型和数据进行相应的处理
        
        return true;
    }

    @Override
    public String getSupportedEventType() {
        return EVENT_TYPE;
    }
} 