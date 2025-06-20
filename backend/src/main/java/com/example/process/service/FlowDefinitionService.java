package com.example.process.service;

import com.example.process.model.common.PageRequest;
import com.example.process.model.common.PageResult;
import com.example.process.model.flow.dto.FlowDefinitionDTO;

import java.util.List;

/**
 * 流程定义服务接口
 */
public interface FlowDefinitionService {

    /**
     * 创建流程定义
     *
     * @param flowDefinitionDTO 流程定义DTO
     * @return 创建后的流程定义DTO
     */
    FlowDefinitionDTO create(FlowDefinitionDTO flowDefinitionDTO);

    /**
     * 更新流程定义
     *
     * @param id               流程定义ID
     * @param flowDefinitionDTO 流程定义DTO
     * @return 更新后的流程定义DTO
     */
    FlowDefinitionDTO update(Long id, FlowDefinitionDTO flowDefinitionDTO);

    /**
     * 根据ID查询流程定义
     *
     * @param id 流程定义ID
     * @return 流程定义DTO
     */
    FlowDefinitionDTO findById(Long id);

    /**
     * 分页查询流程定义
     *
     * @param pageRequest 分页参数
     * @param type        流程类型（可选）
     * @param status      流程状态（可选）
     * @param name        流程名称（可选，模糊查询）
     * @param tag         标签（可选）
     * @param teamId      团队ID（可选）
     * @return 流程定义分页结果
     */
    PageResult<FlowDefinitionDTO> findByPage(PageRequest pageRequest, String type, String status, String name, String tag, Long teamId);

    /**
     * 根据类型查询流程定义
     *
     * @param type 流程类型
     * @return 流程定义列表
     */
    List<FlowDefinitionDTO> findByType(String type);

    /**
     * 根据状态查询流程定义
     *
     * @param status 流程状态
     * @return 流程定义列表
     */
    List<FlowDefinitionDTO> findByStatus(String status);

    /**
     * 根据标签查询流程定义
     *
     * @param tag 标签
     * @return 流程定义列表
     */
    List<FlowDefinitionDTO> findByTag(String tag);

    /**
     * 根据团队ID查询流程定义
     *
     * @param teamId 团队ID
     * @return 流程定义列表
     */
    List<FlowDefinitionDTO> findByTeamId(Long teamId);

    /**
     * 删除流程定义
     *
     * @param id 流程定义ID
     */
    void delete(Long id);

    /**
     * 更新流程定义状态
     *
     * @param id     流程定义ID
     * @param status 新状态
     * @return 更新后的流程定义DTO
     */
    FlowDefinitionDTO updateStatus(Long id, String status);
} 