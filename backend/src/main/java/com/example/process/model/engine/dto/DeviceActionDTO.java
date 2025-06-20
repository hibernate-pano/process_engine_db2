package com.example.process.model.engine.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 设备动作DTO
 */
@Data
public class DeviceActionDTO {

    /**
     * ID
     */
    private Long id;

    /**
     * 流程实例ID
     */
    private Long flowInstanceId;

    /**
     * 流程实例名称
     */
    private String flowInstanceName;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 动作类型
     */
    private String actionType;

    /**
     * 动作名称
     */
    private String actionName;

    /**
     * 动作参数
     */
    private Map<String, Object> parameters;

    /**
     * 状态
     */
    private String status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 计划执行时间
     */
    private LocalDateTime scheduledTime;

    /**
     * 开始执行时间
     */
    private LocalDateTime startTime;

    /**
     * 完成时间
     */
    private LocalDateTime completionTime;

    /**
     * 执行耗时（毫秒）
     */
    private Long duration;

    /**
     * 执行结果
     */
    private Map<String, Object> result;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 最大重试次数
     */
    private Integer maxRetries;

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

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 