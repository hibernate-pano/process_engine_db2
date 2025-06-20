package com.example.process.engine.impl;

import com.example.process.engine.EventHandler;
import com.example.process.engine.FlowEngine;
import com.example.process.engine.NodeExecutor;
import com.example.process.exception.BusinessException;
import com.example.process.model.engine.FlowInstance;
import com.example.process.model.engine.constant.InstanceStatus;
import com.example.process.model.engine.dto.FlowEventDTO;
import com.example.process.model.engine.dto.FlowInstanceDTO;
import com.example.process.model.flow.FlowVersion;
import com.example.process.model.flow.dto.FlowGraph;
import com.example.process.model.flow.dto.FlowNode;
import com.example.process.repository.FlowInstanceRepository;
import com.example.process.repository.FlowVersionRepository;
import com.example.process.service.FlowInstanceService;
import com.example.process.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 流程执行引擎默认实现
 */
@Service
@Slf4j
public class DefaultFlowEngine implements FlowEngine {

    private final FlowInstanceService flowInstanceService;
    private final FlowInstanceRepository flowInstanceRepository;
    private final FlowVersionRepository flowVersionRepository;
    
    // 事件处理器注册表
    private final Map<String, EventHandler> eventHandlers = new ConcurrentHashMap<>();
    
    // 节点执行器注册表
    private final Map<String, NodeExecutor> nodeExecutors = new ConcurrentHashMap<>();

    @Autowired
    public DefaultFlowEngine(
            FlowInstanceService flowInstanceService,
            FlowInstanceRepository flowInstanceRepository,
            FlowVersionRepository flowVersionRepository) {
        this.flowInstanceService = flowInstanceService;
        this.flowInstanceRepository = flowInstanceRepository;
        this.flowVersionRepository = flowVersionRepository;
    }

    @Override
    @Transactional
    public FlowInstanceDTO startInstance(Long instanceId) {
        // 调用服务层启动实例
        FlowInstanceDTO instance = flowInstanceService.startInstance(instanceId);
        
        // 获取流程图
        FlowGraph flowGraph = getFlowGraph(instance.getFlowVersionId());
        if (flowGraph == null) {
            throw new BusinessException("无法获取流程图");
        }
        
        // 找到开始节点
        FlowNode startNode = findStartNode(flowGraph);
        if (startNode == null) {
            throw new BusinessException("流程图中没有开始节点");
        }
        
        // 设置活动节点
        List<String> activeNodes = new ArrayList<>();
        activeNodes.add(startNode.getId());
        
        // 更新实例活动节点
        FlowInstance flowInstance = getFlowInstance(instanceId);
        flowInstance.setActiveNodeIds(JsonUtils.toJsonString(activeNodes));
        flowInstanceRepository.save(flowInstance);
        
        // 执行开始节点
        executeNode(instanceId, startNode.getId(), new HashMap<>());
        
        return flowInstanceService.getInstance(instanceId);
    }

    @Override
    @Transactional
    public FlowInstanceDTO suspendInstance(Long instanceId) {
        // 调用服务层暂停实例
        return flowInstanceService.suspendInstance(instanceId);
    }

    @Override
    @Transactional
    public FlowInstanceDTO resumeInstance(Long instanceId) {
        // 调用服务层恢复实例
        return flowInstanceService.resumeInstance(instanceId);
    }

    @Override
    @Transactional
    public FlowInstanceDTO cancelInstance(Long instanceId) {
        // 调用服务层取消实例
        return flowInstanceService.cancelInstance(instanceId);
    }

    @Override
    @Transactional
    public boolean triggerEvent(FlowEventDTO event) {
        // 获取事件类型
        String eventType = event.getEventType();
        if (!StringUtils.hasText(eventType)) {
            log.error("事件类型为空");
            return false;
        }
        
        // 查找事件处理器
        EventHandler handler = eventHandlers.get(eventType);
        if (handler == null) {
            log.warn("找不到事件类型 [{}] 的处理器", eventType);
            return false;
        }
        
        // 处理事件
        try {
            return handler.handleEvent(event);
        } catch (Exception e) {
            log.error("处理事件时发生错误", e);
            return false;
        }
    }

    @Override
    @Transactional
    public Map<String, Object> executeNode(Long instanceId, String nodeId, Map<String, Object> input) {
        // 获取流程实例
        FlowInstance instance = getFlowInstance(instanceId);
        
        // 检查实例状态
        if (!InstanceStatus.RUNNING.getCode().equals(instance.getStatus())) {
            throw new BusinessException("流程实例不是运行状态，无法执行节点");
        }
        
        // 获取流程图
        FlowGraph flowGraph = getFlowGraph(instance.getFlowVersionId());
        if (flowGraph == null) {
            throw new BusinessException("无法获取流程图");
        }
        
        // 查找节点
        FlowNode node = findNode(flowGraph, nodeId);
        if (node == null) {
            throw new BusinessException("找不到节点: " + nodeId);
        }
        
        // 查找节点执行器
        NodeExecutor executor = nodeExecutors.get(node.getType());
        if (executor == null) {
            throw new BusinessException("找不到节点类型 [" + node.getType() + "] 的执行器");
        }
        
        // 执行节点
        try {
            // 合并输入参数和实例变量
            Map<String, Object> variables = getInstanceVariables(instanceId);
            if (input != null) {
                variables.putAll(input);
            }
            
            // 执行节点
            Map<String, Object> result = executor.execute(instance, node, variables);
            
            // 更新实例变量
            if (result != null && !result.isEmpty()) {
                updateVariables(instanceId, result);
            }
            
            // TODO: 记录执行日志
            
            // TODO: 处理节点执行后的流程流转
            
            return result;
        } catch (Exception e) {
            log.error("执行节点时发生错误", e);
            throw new BusinessException("执行节点时发生错误: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean jumpToNode(Long instanceId, String nodeId) {
        // 获取流程实例
        FlowInstance instance = getFlowInstance(instanceId);
        
        // 检查实例状态
        if (!InstanceStatus.RUNNING.getCode().equals(instance.getStatus())) {
            throw new BusinessException("流程实例不是运行状态，无法跳转节点");
        }
        
        // 获取流程图
        FlowGraph flowGraph = getFlowGraph(instance.getFlowVersionId());
        if (flowGraph == null) {
            throw new BusinessException("无法获取流程图");
        }
        
        // 查找节点
        FlowNode node = findNode(flowGraph, nodeId);
        if (node == null) {
            throw new BusinessException("找不到节点: " + nodeId);
        }
        
        // 设置活动节点
        List<String> activeNodes = new ArrayList<>();
        activeNodes.add(nodeId);
        instance.setActiveNodeIds(JsonUtils.toJsonString(activeNodes));
        flowInstanceRepository.save(instance);
        
        // TODO: 记录跳转日志
        
        return true;
    }

    @Override
    public List<String> getActiveNodes(Long instanceId) {
        return flowInstanceService.getActiveNodes(instanceId);
    }

    @Override
    @Transactional
    public FlowInstanceDTO updateVariables(Long instanceId, Map<String, Object> variables) {
        return flowInstanceService.updateVariables(instanceId, variables);
    }

    @Override
    public Map<String, Object> getInstanceVariables(Long instanceId) {
        return flowInstanceService.getInstanceVariables(instanceId);
    }

    @Override
    public String checkInstanceStatus(Long instanceId) {
        FlowInstanceDTO instance = flowInstanceService.getInstance(instanceId);
        return instance.getStatus();
    }

    @Override
    public void registerEventHandler(String eventType, EventHandler eventHandler) {
        if (StringUtils.hasText(eventType) && eventHandler != null) {
            eventHandlers.put(eventType, eventHandler);
            log.info("注册事件处理器: {}", eventType);
        }
    }

    @Override
    public void registerNodeExecutor(String nodeType, NodeExecutor nodeExecutor) {
        if (StringUtils.hasText(nodeType) && nodeExecutor != null) {
            nodeExecutors.put(nodeType, nodeExecutor);
            log.info("注册节点执行器: {}", nodeType);
        }
    }

    /**
     * 获取流程实例
     *
     * @param instanceId 实例ID
     * @return 流程实例
     */
    private FlowInstance getFlowInstance(Long instanceId) {
        return flowInstanceRepository.findById(instanceId)
                .orElseThrow(() -> new BusinessException("流程实例不存在: " + instanceId));
    }

    /**
     * 获取流程图
     *
     * @param versionId 版本ID
     * @return 流程图
     */
    private FlowGraph getFlowGraph(Long versionId) {
        FlowVersion version = flowVersionRepository.findById(versionId)
                .orElseThrow(() -> new BusinessException("流程版本不存在: " + versionId));
        
        if (!StringUtils.hasText(version.getFlowGraph())) {
            return null;
        }
        
        return JsonUtils.parseObject(version.getFlowGraph(), FlowGraph.class);
    }

    /**
     * 查找开始节点
     *
     * @param flowGraph 流程图
     * @return 开始节点
     */
    private FlowNode findStartNode(FlowGraph flowGraph) {
        if (flowGraph == null || flowGraph.getNodes() == null) {
            return null;
        }
        
        for (FlowNode node : flowGraph.getNodes()) {
            if ("start".equals(node.getType())) {
                return node;
            }
        }
        
        return null;
    }

    /**
     * 查找节点
     *
     * @param flowGraph 流程图
     * @param nodeId    节点ID
     * @return 节点
     */
    private FlowNode findNode(FlowGraph flowGraph, String nodeId) {
        if (flowGraph == null || flowGraph.getNodes() == null || !StringUtils.hasText(nodeId)) {
            return null;
        }
        
        for (FlowNode node : flowGraph.getNodes()) {
            if (nodeId.equals(node.getId())) {
                return node;
            }
        }
        
        return null;
    }
} 