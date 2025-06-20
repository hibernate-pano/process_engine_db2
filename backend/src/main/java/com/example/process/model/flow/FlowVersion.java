package com.example.process.model.flow;

import com.example.process.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程版本实体类
 */
@Data
@Entity
@Table(name = "flow_version")
@EqualsAndHashCode(callSuper = true)
public class FlowVersion extends BaseEntity {

    /**
     * 流程定义ID
     */
    @Column(name = "flow_definition_id", nullable = false)
    private Long flowDefinitionId;

    /**
     * 版本号
     */
    @Column(name = "version", nullable = false)
    private Integer version;

    /**
     * 版本描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 流程数据（JSON格式）
     */
    @Column(name = "flow_data", columnDefinition = "TEXT", nullable = false)
    private String flowData;

    /**
     * 状态（草稿、已发布、已禁用）
     */
    @Column(name = "status", nullable = false)
    private String status;

    /**
     * 发布时间
     */
    @Column(name = "publish_time")
    private java.time.LocalDateTime publishTime;

    /**
     * 发布人
     */
    @Column(name = "publish_by")
    private String publishBy;

    /**
     * 是否为当前版本
     */
    @Column(name = "is_current", nullable = false)
    private Boolean isCurrent = false;
} 