package com.example.process.service.impl;

import com.example.process.exception.BusinessException;
import com.example.process.model.common.PageRequest;
import com.example.process.model.common.PageResult;
import com.example.process.model.engine.FlowInstance;
import com.example.process.model.engine.constant.InstanceStatus;
import com.example.process.model.engine.dto.FlowInstanceDTO;
import com.example.process.model.flow.FlowDefinition;
import com.example.process.model.flow.FlowVersion;
import com.example.process.repository.FlowDefinitionRepository;
import com.example.process.repository.FlowInstanceRepository;
import com.example.process.repository.FlowVersionRepository;
import com.example.process.service.FlowInstanceService;
import com.example.process.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程实例服务实现类
 */
@Service
@Slf4j
public class FlowInstanceServiceImpl implements FlowInstanceService {

    private final FlowInstanceRepository flowInstanceRepository;
    private final FlowDefinitionRepository flowDefinitionRepository;
    private final FlowVersionRepository flowVersionRepository;

    public FlowInstanceServiceImpl(
            FlowInstanceRepository flowInstanceRepository,
            FlowDefinitionRepository flowDefinitionRepository,
            FlowVersionRepository flowVersionRepository) {
        this.flowInstanceRepository = flowInstanceRepository;
        this.flowDefinitionRepository = flowDefinitionRepository;
        this.flowVersionRepository = flowVersionRepository;
    }

    @Override
    @Transactional
    public FlowInstanceDTO createInstance(
            Long flowDefinitionId,
            Long flowVersionId,
            String name,
            String description,
            Map<String, Object> variables,
            Long creatorId,
            Long teamId,
            String priority,
            List<String> tags,
            Long parentInstanceId) {
        
        // 验证流程定义是否存在
        FlowDefinition flowDefinition = flowDefinitionRepository.findById(flowDefinitionId)
                .orElseThrow(() -> new BusinessException("流程定义不存在: " + flowDefinitionId));
        
        // 确定流程版本
        FlowVersion flowVersion;
        if (flowVersionId != null) {
            flowVersion = flowVersionRepository.findById(flowVersionId)
                    .orElseThrow(() -> new BusinessException("流程版本不存在: " + flowVersionId));
            
            if (!flowVersion.getFlowDefinitionId().equals(flowDefinitionId)) {
                throw new BusinessException("流程版本不属于指定的流程定义");
            }
        } else {
            // 使用最新版本
            flowVersion = flowVersionRepository.findTopByFlowDefinitionIdOrderByVersionDesc(flowDefinitionId)
                    .orElseThrow(() -> new BusinessException("流程定义没有可用版本: " + flowDefinitionId));
        }
        
        // 创建流程实例
        FlowInstance instance = new FlowInstance();
        instance.setFlowDefinitionId(flowDefinitionId);
        instance.setFlowVersionId(flowVersion.getId());
        instance.setVersion(flowVersion.getVersion());
        instance.setName(name);
        instance.setDescription(description);
        instance.setStatus(InstanceStatus.CREATED.getCode());
        instance.setCreatorId(creatorId);
        instance.setTeamId(teamId);
        instance.setPriority(priority);
        instance.setParentInstanceId(parentInstanceId);
        
        // 设置初始变量
        if (variables != null && !variables.isEmpty()) {
            instance.setVariables(JsonUtils.toJsonString(variables));
        } else {
            instance.setVariables(JsonUtils.toJsonString(new HashMap<>()));
        }
        
        // 设置标签
        if (tags != null && !tags.isEmpty()) {
            instance.setTags(String.join(",", tags));
        }
        
        // 保存实例
        instance = flowInstanceRepository.save(instance);
        
        return convertToDTO(instance);
    }

    @Override
    @Transactional
    public FlowInstanceDTO startInstance(Long instanceId) {
        FlowInstance instance = getInstanceEntity(instanceId);
        
        // 验证状态
        if (!InstanceStatus.CREATED.getCode().equals(instance.getStatus())) {
            throw new BusinessException("只有处于已创建状态的流程实例才能启动");
        }
        
        // 更新状态
        instance.setStatus(InstanceStatus.RUNNING.getCode());
        instance.setStartTime(LocalDateTime.now());
        
        // 保存实例
        instance = flowInstanceRepository.save(instance);
        
        // TODO: 触发流程执行引擎启动流程
        
        return convertToDTO(instance);
    }

    @Override
    @Transactional
    public FlowInstanceDTO suspendInstance(Long instanceId) {
        FlowInstance instance = getInstanceEntity(instanceId);
        
        // 验证状态
        if (!InstanceStatus.RUNNING.getCode().equals(instance.getStatus())) {
            throw new BusinessException("只有处于运行中状态的流程实例才能暂停");
        }
        
        // 更新状态
        instance.setStatus(InstanceStatus.SUSPENDED.getCode());
        
        // 保存实例
        instance = flowInstanceRepository.save(instance);
        
        // TODO: 触发流程执行引擎暂停流程
        
        return convertToDTO(instance);
    }

    @Override
    @Transactional
    public FlowInstanceDTO resumeInstance(Long instanceId) {
        FlowInstance instance = getInstanceEntity(instanceId);
        
        // 验证状态
        if (!InstanceStatus.SUSPENDED.getCode().equals(instance.getStatus())) {
            throw new BusinessException("只有处于已暂停状态的流程实例才能恢复");
        }
        
        // 更新状态
        instance.setStatus(InstanceStatus.RUNNING.getCode());
        
        // 保存实例
        instance = flowInstanceRepository.save(instance);
        
        // TODO: 触发流程执行引擎恢复流程
        
        return convertToDTO(instance);
    }

    @Override
    @Transactional
    public FlowInstanceDTO cancelInstance(Long instanceId) {
        FlowInstance instance = getInstanceEntity(instanceId);
        
        // 验证状态
        if (InstanceStatus.COMPLETED.getCode().equals(instance.getStatus()) ||
            InstanceStatus.CANCELLED.getCode().equals(instance.getStatus()) ||
            InstanceStatus.FAILED.getCode().equals(instance.getStatus())) {
            throw new BusinessException("已完成、已取消或已失败的流程实例不能再次取消");
        }
        
        // 更新状态
        instance.setStatus(InstanceStatus.CANCELLED.getCode());
        instance.setEndTime(LocalDateTime.now());
        
        // 计算运行时长
        if (instance.getStartTime() != null) {
            // TODO: 计算运行时长
        }
        
        // 保存实例
        instance = flowInstanceRepository.save(instance);
        
        // TODO: 触发流程执行引擎取消流程
        
        return convertToDTO(instance);
    }

    @Override
    public FlowInstanceDTO getInstance(Long instanceId) {
        FlowInstance instance = getInstanceEntity(instanceId);
        return convertToDTO(instance);
    }

    @Override
    @Transactional
    public FlowInstanceDTO updateInstance(
            Long instanceId,
            String name,
            String description,
            String priority,
            List<String> tags) {
        
        FlowInstance instance = getInstanceEntity(instanceId);
        
        // 更新字段
        if (StringUtils.hasText(name)) {
            instance.setName(name);
        }
        
        if (description != null) {
            instance.setDescription(description);
        }
        
        if (StringUtils.hasText(priority)) {
            instance.setPriority(priority);
        }
        
        if (tags != null) {
            instance.setTags(String.join(",", tags));
        }
        
        // 保存实例
        instance = flowInstanceRepository.save(instance);
        
        return convertToDTO(instance);
    }

    @Override
    @Transactional
    public FlowInstanceDTO updateVariables(Long instanceId, Map<String, Object> variables) {
        FlowInstance instance = getInstanceEntity(instanceId);
        
        // 更新变量
        if (variables != null) {
            // 合并变量
            Map<String, Object> existingVariables = getInstanceVariables(instanceId);
            existingVariables.putAll(variables);
            instance.setVariables(JsonUtils.toJsonString(existingVariables));
        }
        
        // 保存实例
        instance = flowInstanceRepository.save(instance);
        
        return convertToDTO(instance);
    }

    @Override
    public PageResult<FlowInstanceDTO> listInstances(
            PageRequest pageRequest,
            Long flowDefinitionId,
            String status,
            Long creatorId,
            Long teamId,
            String name,
            String tag) {
        
        // 创建分页对象
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPageNum() - 1,
                pageRequest.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createTime"));
        
        Page<FlowInstance> page;
        
        if (StringUtils.hasText(name)) {
            // 按名称查询
            page = flowInstanceRepository.findByNameContaining(name, pageable);
        } else if (StringUtils.hasText(tag)) {
            // 按标签查询
            page = flowInstanceRepository.findByTag(tag, pageable);
        } else {
            // 按条件组合查询
            page = flowInstanceRepository.findByConditions(
                    flowDefinitionId,
                    status,
                    creatorId,
                    teamId,
                    pageable);
        }
        
        // 转换为DTO
        List<FlowInstanceDTO> dtoList = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        // 创建分页结果
        return new PageResult<>(
                dtoList,
                page.getTotalElements(),
                pageRequest.getPageNum(),
                pageRequest.getPageSize());
    }

    @Override
    public List<FlowInstanceDTO> listInstancesByDefinition(Long flowDefinitionId) {
        List<FlowInstance> instances = flowInstanceRepository.findByFlowDefinitionId(flowDefinitionId);
        return instances.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowInstanceDTO> listInstancesByVersion(Long flowVersionId) {
        List<FlowInstance> instances = flowInstanceRepository.findByFlowVersionId(flowVersionId);
        return instances.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowInstanceDTO> listChildInstances(Long parentInstanceId) {
        List<FlowInstance> instances = flowInstanceRepository.findByParentInstanceId(parentInstanceId);
        return instances.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteInstance(Long instanceId) {
        FlowInstance instance = getInstanceEntity(instanceId);
        
        // 验证状态
        if (InstanceStatus.RUNNING.getCode().equals(instance.getStatus()) ||
            InstanceStatus.SUSPENDED.getCode().equals(instance.getStatus())) {
            throw new BusinessException("运行中或已暂停的流程实例不能删除");
        }
        
        // 删除实例
        flowInstanceRepository.delete(instance);
        
        // TODO: 删除相关的执行记录、设备动作等
    }

    @Override
    public Map<String, Long> getInstanceStatusStatistics() {
        Map<String, Long> statistics = new HashMap<>();
        
        // 统计各状态的实例数量
        for (InstanceStatus status : InstanceStatus.values()) {
            long count = flowInstanceRepository.countByStatus(status.getCode());
            statistics.put(status.getCode(), count);
        }
        
        return statistics;
    }

    @Override
    public Map<String, Object> getInstanceVariables(Long instanceId) {
        FlowInstance instance = getInstanceEntity(instanceId);
        
        // 解析变量
        if (StringUtils.hasText(instance.getVariables())) {
            return JsonUtils.parseObject(instance.getVariables(), Map.class);
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public List<String> getActiveNodes(Long instanceId) {
        FlowInstance instance = getInstanceEntity(instanceId);
        
        // 解析活动节点
        if (StringUtils.hasText(instance.getActiveNodeIds())) {
            return JsonUtils.parseArray(instance.getActiveNodeIds(), String.class);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 获取流程实例实体
     *
     * @param instanceId 实例ID
     * @return 流程实例实体
     */
    private FlowInstance getInstanceEntity(Long instanceId) {
        return flowInstanceRepository.findById(instanceId)
                .orElseThrow(() -> new BusinessException("流程实例不存在: " + instanceId));
    }

    /**
     * 将实体转换为DTO
     *
     * @param instance 流程实例实体
     * @return 流程实例DTO
     */
    private FlowInstanceDTO convertToDTO(FlowInstance instance) {
        FlowInstanceDTO dto = new FlowInstanceDTO();
        BeanUtils.copyProperties(instance, dto);
        
        // 设置状态描述
        try {
            InstanceStatus status = InstanceStatus.fromCode(instance.getStatus());
            dto.setStatusDesc(status.getDescription());
        } catch (IllegalArgumentException e) {
            dto.setStatusDesc("未知状态");
        }
        
        // 解析活动节点
        if (StringUtils.hasText(instance.getActiveNodeIds())) {
            dto.setActiveNodeIds(JsonUtils.parseArray(instance.getActiveNodeIds(), String.class));
        } else {
            dto.setActiveNodeIds(new ArrayList<>());
        }
        
        // 解析变量
        if (StringUtils.hasText(instance.getVariables())) {
            dto.setVariables(JsonUtils.parseObject(instance.getVariables(), Map.class));
        } else {
            dto.setVariables(new HashMap<>());
        }
        
        // 解析标签
        if (StringUtils.hasText(instance.getTags())) {
            dto.setTags(Arrays.asList(instance.getTags().split(",")));
        } else {
            dto.setTags(new ArrayList<>());
        }
        
        // 计算运行时长
        if (instance.getStartTime() != null && instance.getEndTime() != null) {
            // TODO: 计算运行时长
        }
        
        // 查询流程定义名称
        try {
            flowDefinitionRepository.findById(instance.getFlowDefinitionId())
                    .ifPresent(definition -> dto.setFlowDefinitionName(definition.getName()));
        } catch (Exception e) {
            log.warn("获取流程定义名称失败", e);
        }
        
        return dto;
    }
} 