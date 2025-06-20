package com.example.process.service;

import com.example.process.model.common.PageRequest;
import com.example.process.model.common.PageResult;
import com.example.process.model.engine.dto.FlowInstanceDTO;

import java.util.List;
import java.util.Map;

/**
 * 流程实例服务接口
 */
public interface FlowInstanceService {

    /**
     * 创建流程实例
     *
     * @param flowDefinitionId 流程定义ID
     * @param flowVersionId    流程版本ID（可选，如果不指定则使用最新版本）
     * @param name             实例名称
     * @param description      实例描述
     * @param variables        初始变量
     * @param creatorId        创建者ID
     * @param teamId           团队ID
     * @param priority         优先级
     * @param tags             标签列表
     * @param parentInstanceId 父流程实例ID（用于子流程）
     * @return 创建的流程实例
     */
    FlowInstanceDTO createInstance(
            Long flowDefinitionId,
            Long flowVersionId,
            String name,
            String description,
            Map<String, Object> variables,
            Long creatorId,
            Long teamId,
            String priority,
            List<String> tags,
            Long parentInstanceId);

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
     * 根据ID查询流程实例
     *
     * @param instanceId 流程实例ID
     * @return 流程实例
     */
    FlowInstanceDTO getInstance(Long instanceId);

    /**
     * 更新流程实例
     *
     * @param instanceId   流程实例ID
     * @param name         实例名称
     * @param description  实例描述
     * @param priority     优先级
     * @param tags         标签列表
     * @return 更新后的流程实例
     */
    FlowInstanceDTO updateInstance(
            Long instanceId,
            String name,
            String description,
            String priority,
            List<String> tags);

    /**
     * 更新流程实例变量
     *
     * @param instanceId 流程实例ID
     * @param variables  变量
     * @return 更新后的流程实例
     */
    FlowInstanceDTO updateVariables(Long instanceId, Map<String, Object> variables);

    /**
     * 分页查询流程实例
     *
     * @param pageRequest      分页请求
     * @param flowDefinitionId 流程定义ID（可选）
     * @param status           状态（可选）
     * @param creatorId        创建者ID（可选）
     * @param teamId           团队ID（可选）
     * @param name             名称关键字（可选）
     * @param tag              标签关键字（可选）
     * @return 流程实例分页结果
     */
    PageResult<FlowInstanceDTO> listInstances(
            PageRequest pageRequest,
            Long flowDefinitionId,
            String status,
            Long creatorId,
            Long teamId,
            String name,
            String tag);

    /**
     * 查询流程定义的所有实例
     *
     * @param flowDefinitionId 流程定义ID
     * @return 流程实例列表
     */
    List<FlowInstanceDTO> listInstancesByDefinition(Long flowDefinitionId);

    /**
     * 查询流程版本的所有实例
     *
     * @param flowVersionId 流程版本ID
     * @return 流程实例列表
     */
    List<FlowInstanceDTO> listInstancesByVersion(Long flowVersionId);

    /**
     * 查询子流程实例
     *
     * @param parentInstanceId 父流程实例ID
     * @return 子流程实例列表
     */
    List<FlowInstanceDTO> listChildInstances(Long parentInstanceId);

    /**
     * 删除流程实例
     *
     * @param instanceId 流程实例ID
     */
    void deleteInstance(Long instanceId);

    /**
     * 获取流程实例状态统计
     *
     * @return 状态统计结果，格式为：{status: count}
     */
    Map<String, Long> getInstanceStatusStatistics();

    /**
     * 获取流程实例变量
     *
     * @param instanceId 流程实例ID
     * @return 变量
     */
    Map<String, Object> getInstanceVariables(Long instanceId);

    /**
     * 获取流程实例活动节点
     *
     * @param instanceId 流程实例ID
     * @return 活动节点ID列表
     */
    List<String> getActiveNodes(Long instanceId);
} 