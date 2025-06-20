package com.example.process.engine;

import com.example.process.model.engine.dto.FlowEventDTO;
import com.example.process.model.engine.dto.FlowInstanceDTO;

import java.util.List;
import java.util.Map;

/**
 * 流程执行引擎接口
 * 负责流程实例的执行、控制和状态管理
 */
public interface FlowEngine {

    /**
     * 启动流程实例
     *
     * @param instanceId 流程实例ID
     * @return 启动后的流程实例
     */
    FlowInstanceDTO startInstance(Long instanceId);

    /**
     * 暂停流程实例
     *
     * @param instanceId 流程实例ID
     * @return 暂停后的流程实例
     */
    FlowInstanceDTO suspendInstance(Long instanceId);

    /**
     * 恢复流程实例
     *
     * @param instanceId 流程实例ID
     * @return 恢复后的流程实例
     */
    FlowInstanceDTO resumeInstance(Long instanceId);

    /**
     * 取消流程实例
     *
     * @param instanceId 流程实例ID
     * @return 取消后的流程实例
     */
    FlowInstanceDTO cancelInstance(Long instanceId);

    /**
     * 触发事件
     *
     * @param event 事件
     * @return 处理结果
     */
    boolean triggerEvent(FlowEventDTO event);

    /**
     * 执行特定节点
     *
     * @param instanceId 流程实例ID
     * @param nodeId     节点ID
     * @param input      输入参数
     * @return 执行结果
     */
    Map<String, Object> executeNode(Long instanceId, String nodeId, Map<String, Object> input);

    /**
     * 跳转到指定节点
     *
     * @param instanceId 流程实例ID
     * @param nodeId     目标节点ID
     * @return 执行结果
     */
    boolean jumpToNode(Long instanceId, String nodeId);

    /**
     * 获取流程实例当前活动节点
     *
     * @param instanceId 流程实例ID
     * @return 活动节点ID列表
     */
    List<String> getActiveNodes(Long instanceId);

    /**
     * 更新流程实例变量
     *
     * @param instanceId 流程实例ID
     * @param variables  变量
     * @return 更新后的流程实例
     */
    FlowInstanceDTO updateVariables(Long instanceId, Map<String, Object> variables);

    /**
     * 获取流程实例变量
     *
     * @param instanceId 流程实例ID
     * @return 变量
     */
    Map<String, Object> getInstanceVariables(Long instanceId);

    /**
     * 检查流程实例状态
     *
     * @param instanceId 流程实例ID
     * @return 流程实例状态
     */
    String checkInstanceStatus(Long instanceId);

    /**
     * 注册事件处理器
     *
     * @param eventType    事件类型
     * @param eventHandler 事件处理器
     */
    void registerEventHandler(String eventType, EventHandler eventHandler);

    /**
     * 注册节点执行器
     *
     * @param nodeType     节点类型
     * @param nodeExecutor 节点执行器
     */
    void registerNodeExecutor(String nodeType, NodeExecutor nodeExecutor);
} 