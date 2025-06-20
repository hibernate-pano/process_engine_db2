package com.example.process.service;

import com.example.process.model.common.PageRequest;
import com.example.process.model.common.PageResult;
import com.example.process.model.flow.dto.FlowVersionDTO;

import java.util.List;

/**
 * 流程版本服务接口
 */
public interface FlowVersionService {

    /**
     * 创建流程版本
     *
     * @param flowVersionDTO 流程版本DTO
     * @return 创建后的流程版本DTO
     */
    FlowVersionDTO create(FlowVersionDTO flowVersionDTO);

    /**
     * 更新流程版本
     *
     * @param id            流程版本ID
     * @param flowVersionDTO 流程版本DTO
     * @return 更新后的流程版本DTO
     */
    FlowVersionDTO update(Long id, FlowVersionDTO flowVersionDTO);

    /**
     * 根据ID查询流程版本
     *
     * @param id 流程版本ID
     * @return 流程版本DTO
     */
    FlowVersionDTO findById(Long id);

    /**
     * 根据流程定义ID和版本号查询流程版本
     *
     * @param flowDefinitionId 流程定义ID
     * @param version         版本号
     * @return 流程版本DTO
     */
    FlowVersionDTO findByFlowDefinitionIdAndVersion(Long flowDefinitionId, Integer version);

    /**
     * 根据流程定义ID查询当前版本
     *
     * @param flowDefinitionId 流程定义ID
     * @return 流程版本DTO
     */
    FlowVersionDTO findCurrentVersionByFlowDefinitionId(Long flowDefinitionId);

    /**
     * 根据流程定义ID查询所有版本
     *
     * @param flowDefinitionId 流程定义ID
     * @return 流程版本列表
     */
    List<FlowVersionDTO> findAllByFlowDefinitionId(Long flowDefinitionId);

    /**
     * 根据流程定义ID分页查询版本
     *
     * @param flowDefinitionId 流程定义ID
     * @param pageRequest     分页参数
     * @return 流程版本分页结果
     */
    PageResult<FlowVersionDTO> findByFlowDefinitionIdAndPage(Long flowDefinitionId, PageRequest pageRequest);

    /**
     * 发布流程版本
     *
     * @param id 流程版本ID
     * @return 发布后的流程版本DTO
     */
    FlowVersionDTO publish(Long id);

    /**
     * 禁用流程版本
     *
     * @param id 流程版本ID
     * @return 禁用后的流程版本DTO
     */
    FlowVersionDTO disable(Long id);

    /**
     * 设置为当前版本
     *
     * @param id 流程版本ID
     * @return 设置后的流程版本DTO
     */
    FlowVersionDTO setAsCurrent(Long id);

    /**
     * 删除流程版本
     *
     * @param id 流程版本ID
     */
    void delete(Long id);

    /**
     * 创建新版本
     *
     * @param flowDefinitionId 流程定义ID
     * @param flowVersionDTO   流程版本DTO（不需要设置版本号，会自动生成）
     * @return 创建后的流程版本DTO
     */
    FlowVersionDTO createNewVersion(Long flowDefinitionId, FlowVersionDTO flowVersionDTO);
} 