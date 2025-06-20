package com.example.process.model.flow;

import com.example.process.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程定义实体类
 */
@Data
@Entity
@Table(name = "flow_definition")
@EqualsAndHashCode(callSuper = true)
public class FlowDefinition extends BaseEntity {

    /**
     * 流程名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 流程描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 流程类型（预案、任务等）
     */
    @Column(name = "type", nullable = false)
    private String type;

    /**
     * 标签（多个标签用逗号分隔）
     */
    @Column(name = "tags")
    private String tags;

    /**
     * 当前版本号
     */
    @Column(name = "current_version", nullable = false)
    private Integer currentVersion = 1;

    /**
     * 状态（草稿、已发布、已禁用）
     */
    @Column(name = "status", nullable = false)
    private String status;

    /**
     * 所属团队/部门ID
     */
    @Column(name = "team_id")
    private Long teamId;
} 