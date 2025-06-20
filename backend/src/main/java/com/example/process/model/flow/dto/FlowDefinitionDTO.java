package com.example.process.model.flow.dto;

import com.example.process.model.flow.FlowDefinition;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 流程定义数据传输对象
 */
@Data
public class FlowDefinitionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程定义ID
     */
    private Long id;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程描述
     */
    private String description;

    /**
     * 流程类型（预案、任务等）
     */
    private String type;

    /**
     * 标签（多个标签用逗号分隔）
     */
    private String tags;

    /**
     * 当前版本号
     */
    private Integer currentVersion;

    /**
     * 状态（草稿、已发布、已禁用）
     */
    private String status;

    /**
     * 所属团队/部门ID
     */
    private Long teamId;

    /**
     * 所属团队/部门名称
     */
    private String teamName;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 从实体转换为DTO
     *
     * @param flowDefinition 流程定义实体
     * @return 流程定义DTO
     */
    public static FlowDefinitionDTO fromEntity(FlowDefinition flowDefinition) {
        if (flowDefinition == null) {
            return null;
        }
        
        FlowDefinitionDTO dto = new FlowDefinitionDTO();
        dto.setId(flowDefinition.getId());
        dto.setName(flowDefinition.getName());
        dto.setDescription(flowDefinition.getDescription());
        dto.setType(flowDefinition.getType());
        dto.setTags(flowDefinition.getTags());
        dto.setCurrentVersion(flowDefinition.getCurrentVersion());
        dto.setStatus(flowDefinition.getStatus());
        dto.setTeamId(flowDefinition.getTeamId());
        dto.setCreatedAt(flowDefinition.getCreatedAt());
        dto.setCreatedBy(flowDefinition.getCreatedBy());
        dto.setUpdatedAt(flowDefinition.getUpdatedAt());
        dto.setUpdatedBy(flowDefinition.getUpdatedBy());
        
        return dto;
    }

    /**
     * 转换为实体
     *
     * @return 流程定义实体
     */
    public FlowDefinition toEntity() {
        FlowDefinition entity = new FlowDefinition();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setDescription(this.description);
        entity.setType(this.type);
        entity.setTags(this.tags);
        entity.setCurrentVersion(this.currentVersion != null ? this.currentVersion : 1);
        entity.setStatus(this.status);
        entity.setTeamId(this.teamId);
        
        return entity;
    }
} 