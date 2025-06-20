package com.example.process.model.engine;

import com.example.process.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备动作实体类
 * 用于表示流程中的设备动作
 */
@Data
@Entity
@Table(name = "device_action")
@EqualsAndHashCode(callSuper = true)
public class DeviceAction extends BaseEntity {

    /**
     * 流程实例ID
     */
    @Column(name = "flow_instance_id", nullable = false)
    private Long flowInstanceId;

    /**
     * 节点ID
     */
    @Column(name = "node_id", nullable = false)
    private String nodeId;

    /**
     * 设备ID
     */
    @Column(name = "device_id", nullable = false)
    private String deviceId;

    /**
     * 设备类型
     */
    @Column(name = "device_type")
    private String deviceType;

    /**
     * 设备名称
     */
    @Column(name = "device_name")
    private String deviceName;

    /**
     * 动作类型
     */
    @Column(name = "action_type", nullable = false)
    private String actionType;

    /**
     * 动作名称
     */
    @Column(name = "action_name")
    private String actionName;

    /**
     * 动作参数（JSON格式）
     */
    @Column(name = "parameters", columnDefinition = "TEXT")
    private String parameters;

    /**
     * 状态（等待执行、执行中、已完成、已取消、失败）
     */
    @Column(name = "status", nullable = false)
    private String status;

    /**
     * 计划执行时间
     */
    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    /**
     * 开始执行时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * 完成时间
     */
    @Column(name = "completion_time")
    private LocalDateTime completionTime;

    /**
     * 执行耗时（毫秒）
     */
    @Column(name = "duration")
    private Long duration;

    /**
     * 执行结果（JSON格式）
     */
    @Column(name = "result", columnDefinition = "TEXT")
    private String result;

    /**
     * 错误信息
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    /**
     * 重试次数
     */
    @Column(name = "retry_count")
    private Integer retryCount;

    /**
     * 最大重试次数
     */
    @Column(name = "max_retries")
    private Integer maxRetries;

    /**
     * 优先级（高、中、低）
     */
    @Column(name = "priority")
    private String priority;

    /**
     * 备注
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
} 