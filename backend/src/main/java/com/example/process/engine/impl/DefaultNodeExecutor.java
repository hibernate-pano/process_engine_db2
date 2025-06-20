package com.example.process.engine.impl;

import com.example.process.engine.NodeExecutor;
import com.example.process.model.engine.FlowInstance;
import com.example.process.model.flow.dto.FlowNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认节点执行器实现
 * 处理基本节点类型
 */
@Component
@Slf4j
public class DefaultNodeExecutor implements NodeExecutor {

    private static final String NODE_TYPE = "default";

    @Override
    public Map<String, Object> execute(FlowInstance instance, FlowNode node, Map<String, Object> input) {
        log.info("执行默认节点: {}, 实例ID: {}", node.getId(), instance.getId());
        
        // 创建输出结果
        Map<String, Object> output = new HashMap<>();
        
        // 默认节点只是简单地传递输入参数
        if (input != null) {
            output.putAll(input);
        }
        
        // 添加节点执行信息
        output.put("nodeId", node.getId());
        output.put("nodeName", node.getName());
        output.put("nodeType", node.getType());
        output.put("executionTime", System.currentTimeMillis());
        
        return output;
    }

    @Override
    public String getSupportedNodeType() {
        return NODE_TYPE;
    }
} 