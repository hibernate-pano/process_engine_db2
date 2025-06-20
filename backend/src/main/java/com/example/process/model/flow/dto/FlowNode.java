package com.example.process.model.flow.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程节点数据传输对象
 */
@Data
public class FlowNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    private String id;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点类型
     */
    private String type;

    /**
     * 节点标签
     */
    private String label;

    /**
     * 节点位置X坐标
     */
    private Integer x;

    /**
     * 节点位置Y坐标
     */
    private Integer y;

    /**
     * 节点宽度
     */
    private Integer width;

    /**
     * 节点高度
     */
    private Integer height;

    /**
     * 节点样式
     */
    private Map<String, Object> style = new HashMap<>();

    /**
     * 节点数据
     */
    private Map<String, Object> data = new HashMap<>();

    /**
     * 节点配置属性
     */
    private Map<String, Object> properties;
} 