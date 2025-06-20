package com.example.process.controller;

import com.example.process.engine.FlowEngine;
import com.example.process.model.engine.dto.FlowEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 事件控制器
 */
@RestController
@RequestMapping("/api/flow-events")
public class FlowEventController {

    private final FlowEngine flowEngine;

    @Autowired
    public FlowEventController(FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
    }

    /**
     * 触发事件
     *
     * @param event 事件
     * @return 处理结果
     */
    @PostMapping("/trigger")
    public ResponseEntity<Boolean> triggerEvent(@RequestBody FlowEventDTO event) {
        boolean result = flowEngine.triggerEvent(event);
        return ResponseEntity.ok(result);
    }

    /**
     * 执行节点
     *
     * @param instanceId 流程实例ID
     * @param nodeId     节点ID
     * @param input      输入参数
     * @return 执行结果
     */
    @PostMapping("/execute-node")
    public ResponseEntity<Object> executeNode(
            @RequestParam Long instanceId,
            @RequestParam String nodeId,
            @RequestBody(required = false) Object input) {
        Object result = flowEngine.executeNode(instanceId, nodeId, (input instanceof java.util.Map) ? 
                (java.util.Map<String, Object>) input : new java.util.HashMap<>());
        return ResponseEntity.ok(result);
    }

    /**
     * 跳转到指定节点
     *
     * @param instanceId 流程实例ID
     * @param nodeId     目标节点ID
     * @return 执行结果
     */
    @PostMapping("/jump-to-node")
    public ResponseEntity<Boolean> jumpToNode(
            @RequestParam Long instanceId,
            @RequestParam String nodeId) {
        boolean result = flowEngine.jumpToNode(instanceId, nodeId);
        return ResponseEntity.ok(result);
    }
} 