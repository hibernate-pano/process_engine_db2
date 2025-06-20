package com.example.process.model.engine.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 事件DTO
 */
@Data
public class FlowEventDTO {

    /**
     * ID
     */
    private Long id;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件类型描述
     */
    private String eventTypeDesc;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件源ID
     */
    private String sourceId;

    /**
     * 事件源类型
     */
    private String sourceType;

    /**
     * 事件源名称
     */
    private String sourceName;

    /**
     * 事件数据
     */
    private Map<String, Object> eventData;

    /**
     * 事件发生时间
     */
    private LocalDateTime occurrenceTime;

    /**
     * 事件处理时间
     */
    private LocalDateTime processingTime;

    /**
     * 事件处理状态
     */
    private String status;

    /**
     * 事件处理状态描述
     */
    private String statusDesc;

    /**
     * 关联的流程实例ID
     */
    private Long flowInstanceId;

    /**
     * 关联的流程实例名称
     */
    private String flowInstanceName;

    /**
     * 关联的节点ID
     */
    private String nodeId;

    /**
     * 关联的节点名称
     */
    private String nodeName;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 