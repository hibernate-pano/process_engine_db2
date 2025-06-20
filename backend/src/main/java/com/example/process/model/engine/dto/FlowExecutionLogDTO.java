package com.example.process.model.engine.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 流程执行记录DTO
 */
@Data
public class FlowExecutionLogDTO {

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
     * 节点类型
     */
    private String nodeType;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 执行类型
     */
    private String executionType;

    /**
     * 执行类型描述
     */
    private String executionTypeDesc;

    /**
     * 执行状态
     */
    private String status;

    /**
     * 执行状态描述
     */
    private String statusDesc;

    /**
     * 执行时间
     */
    private LocalDateTime executionTime;

    /**
     * 执行耗时（毫秒）
     */
    private Long duration;

    /**
     * 执行输入
     */
    private Map<String, Object> input;

    /**
     * 执行输出
     */
    private Map<String, Object> output;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 执行者ID
     */
    private String executorId;

    /**
     * 执行者类型
     */
    private String executorType;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 