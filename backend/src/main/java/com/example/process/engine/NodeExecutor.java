package com.example.process.engine;

import com.example.process.model.engine.FlowInstance;
import com.example.process.model.flow.dto.FlowNode;

import java.util.Map;

/**
 * 节点执行器接口
 * 用于执行流程中的各类节点
 */
public interface NodeExecutor {

    /**
     * 执行节点
     *
     * @param instance 流程实例
     * @param node     节点
     * @param input    输入参数
     * @return 执行结果
     */
    Map<String, Object> execute(FlowInstance instance, FlowNode node, Map<String, Object> input);

    /**
     * 获取支持的节点类型
     *
     * @return 节点类型
     */
    String getSupportedNodeType();
} 