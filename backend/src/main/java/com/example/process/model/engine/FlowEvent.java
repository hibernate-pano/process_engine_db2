package com.example.process.model.engine;

import com.example.process.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 事件实体类
 * 用于表示流程中的事件触发
 */
@Data
@Entity
@Table(name = "flow_event")
@EqualsAndHashCode(callSuper = true)
public class FlowEvent extends BaseEntity {

    /**
     * 事件类型
     */
    @Column(name = "event_type", nullable = false)
    private String eventType;

    /**
     * 事件名称
     */
    @Column(name = "event_name")
    private String eventName;

    /**
     * 事件源（设备ID、系统ID等）
     */
    @Column(name = "source_id")
    private String sourceId;

    /**
     * 事件源类型（设备、系统、用户等）
     */
    @Column(name = "source_type")
    private String sourceType;

    /**
     * 事件数据（JSON格式）
     */
    @Column(name = "event_data", columnDefinition = "TEXT")
    private String eventData;

    /**
     * 事件发生时间
     */
    @Column(name = "occurrence_time", nullable = false)
    private LocalDateTime occurrenceTime;

    /**
     * 事件处理时间
     */
    @Column(name = "processing_time")
    private LocalDateTime processingTime;

    /**
     * 事件处理状态（未处理、处理中、已处理、忽略）
     */
    @Column(name = "status", nullable = false)
    private String status;

    /**
     * 关联的流程实例ID
     */
    @Column(name = "flow_instance_id")
    private Long flowInstanceId;

    /**
     * 关联的节点ID
     */
    @Column(name = "node_id")
    private String nodeId;

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