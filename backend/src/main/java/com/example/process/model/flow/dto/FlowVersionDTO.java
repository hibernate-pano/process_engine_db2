package com.example.process.model.flow.dto;

import com.example.process.model.flow.FlowVersion;
import com.example.process.util.JsonUtils;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 流程版本数据传输对象
 */
@Data
public class FlowVersionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程版本ID
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
     * 版本号
     */
    private Integer version;

    /**
     * 版本描述
     */
    private String description;

    /**
     * 流程数据（JSON格式）
     */
    private String flowData;

    /**
     * 流程图数据对象
     */
    private FlowGraph flowGraph;

    /**
     * 状态（草稿、已发布、已禁用）
     */
    private String status;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 发布人
     */
    private String publishBy;

    /**
     * 是否为当前版本
     */
    private Boolean isCurrent;

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
     * @param flowVersion 流程版本实体
     * @return 流程版本DTO
     */
    public static FlowVersionDTO fromEntity(FlowVersion flowVersion) {
        if (flowVersion == null) {
            return null;
        }
        
        FlowVersionDTO dto = new FlowVersionDTO();
        dto.setId(flowVersion.getId());
        dto.setFlowDefinitionId(flowVersion.getFlowDefinitionId());
        dto.setVersion(flowVersion.getVersion());
        dto.setDescription(flowVersion.getDescription());
        dto.setFlowData(flowVersion.getFlowData());
        
        // 解析流程图数据
        if (flowVersion.getFlowData() != null && !flowVersion.getFlowData().isEmpty()) {
            dto.setFlowGraph(JsonUtils.parseObject(flowVersion.getFlowData(), FlowGraph.class));
        }
        
        dto.setStatus(flowVersion.getStatus());
        dto.setPublishTime(flowVersion.getPublishTime());
        dto.setPublishBy(flowVersion.getPublishBy());
        dto.setIsCurrent(flowVersion.getIsCurrent());
        dto.setCreatedAt(flowVersion.getCreatedAt());
        dto.setCreatedBy(flowVersion.getCreatedBy());
        dto.setUpdatedAt(flowVersion.getUpdatedAt());
        dto.setUpdatedBy(flowVersion.getUpdatedBy());
        
        return dto;
    }

    /**
     * 转换为实体
     *
     * @return 流程版本实体
     */
    public FlowVersion toEntity() {
        FlowVersion entity = new FlowVersion();
        entity.setId(this.id);
        entity.setFlowDefinitionId(this.flowDefinitionId);
        entity.setVersion(this.version);
        entity.setDescription(this.description);
        
        // 如果存在流程图对象，则转换为JSON字符串
        if (this.flowGraph != null) {
            entity.setFlowData(JsonUtils.toJsonString(this.flowGraph));
        } else {
            entity.setFlowData(this.flowData);
        }
        
        entity.setStatus(this.status);
        entity.setPublishTime(this.publishTime);
        entity.setPublishBy(this.publishBy);
        entity.setIsCurrent(this.isCurrent != null ? this.isCurrent : false);
        
        return entity;
    }
} 