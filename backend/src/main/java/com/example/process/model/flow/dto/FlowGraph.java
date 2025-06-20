package com.example.process.model.flow.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程图数据传输对象
 */
@Data
public class FlowGraph implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程图ID
     */
    private String id;

    /**
     * 流程图名称
     */
    private String name;

    /**
     * 流程图描述
     */
    private String description;

    /**
     * 节点列表
     */
    private List<FlowNode> nodes = new ArrayList<>();

    /**
     * 边列表
     */
    private List<FlowEdge> edges = new ArrayList<>();

    /**
     * 流程图元数据
     */
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * 流程图视图配置
     */
    private Map<String, Object> viewport = new HashMap<>();

    /**
     * 流程图全局变量
     */
    private Map<String, Object> variables = new HashMap<>();

    /**
     * 流程图设备配置
     */
    private Map<String, Object> devices = new HashMap<>();
} 