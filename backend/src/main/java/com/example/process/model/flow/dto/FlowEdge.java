package com.example.process.model.flow.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程边数据传输对象
 */
@Data
public class FlowEdge implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 边ID
     */
    private String id;

    /**
     * 源节点ID
     */
    private String source;

    /**
     * 目标节点ID
     */
    private String target;

    /**
     * 源节点锚点
     */
    private String sourceHandle;

    /**
     * 目标节点锚点
     */
    private String targetHandle;

    /**
     * 边标签
     */
    private String label;

    /**
     * 边类型
     */
    private String type;

    /**
     * 边样式
     */
    private Map<String, Object> style = new HashMap<>();

    /**
     * 边数据
     */
    private Map<String, Object> data = new HashMap<>();

    /**
     * 是否为条件边
     */
    private Boolean isConditional = false;

    /**
     * 条件表达式
     */
    private String conditionExpression;
} 