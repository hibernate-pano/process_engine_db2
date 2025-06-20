package com.example.process.model.engine;

import com.example.process.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 流程实例实体类
 * 表示一个正在执行的流程
 */
@Data
@Entity
@Table(name = "flow_instance")
@EqualsAndHashCode(callSuper = true)
public class FlowInstance extends BaseEntity {

    /**
     * 流程定义ID
     */
    @Column(name = "flow_definition_id", nullable = false)
    private Long flowDefinitionId;

    /**
     * 流程版本ID
     */
    @Column(name = "flow_version_id", nullable = false)
    private Long flowVersionId;

    /**
     * 流程版本号
     */
    @Column(name = "version", nullable = false)
    private Integer version;

    /**
     * 流程实例名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 流程实例描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 状态（已创建、运行中、已暂停、已完成、已取消、失败）
     */
    @Column(name = "status", nullable = false)
    private String status;

    /**
     * 当前活动节点ID列表（JSON数组格式）
     */
    @Column(name = "active_node_ids", columnDefinition = "TEXT")
    private String activeNodeIds;

    /**
     * 流程变量（JSON格式）
     */
    @Column(name = "variables", columnDefinition = "TEXT")
    private String variables;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 创建者ID
     */
    @Column(name = "creator_id")
    private Long creatorId;

    /**
     * 所属团队/部门ID
     */
    @Column(name = "team_id")
    private Long teamId;

    /**
     * 优先级（高、中、低）
     */
    @Column(name = "priority")
    private String priority;

    /**
     * 标签（多个标签用逗号分隔）
     */
    @Column(name = "tags")
    private String tags;

    /**
     * 父流程实例ID（用于子流程）
     */
    @Column(name = "parent_instance_id")
    private Long parentInstanceId;
} 