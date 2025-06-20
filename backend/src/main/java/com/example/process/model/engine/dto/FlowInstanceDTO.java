package com.example.process.model.engine.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 流程实例DTO
 */
@Data
public class FlowInstanceDTO {

    /**
     * ID
     */
    private Long id;

    /**
     * 流程定义ID
     */
    private Long flowDefinitionId;

    /**
     * 流程定义名称
     */
    private String flowDefinitionName;

    /**
     * 流程版本ID
     */
    private Long flowVersionId;

    /**
     * 流程版本号
     */
    private Integer version;

    /**
     * 流程实例名称
     */
    private String name;

    /**
     * 流程实例描述
     */
    private String description;

    /**
     * 状态
     */
    private String status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 当前活动节点ID列表
     */
    private List<String> activeNodeIds;

    /**
     * 流程变量
     */
    private Map<String, Object> variables;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 运行时长（毫秒）
     */
    private Long duration;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建者名称
     */
    private String creatorName;

    /**
     * 所属团队/部门ID
     */
    private Long teamId;

    /**
     * 所属团队/部门名称
     */
    private String teamName;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 父流程实例ID
     */
    private Long parentInstanceId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 