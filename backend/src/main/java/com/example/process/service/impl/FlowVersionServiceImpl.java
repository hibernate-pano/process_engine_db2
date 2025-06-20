package com.example.process.service.impl;

import com.example.process.exception.BusinessException;
import com.example.process.model.common.PageRequest;
import com.example.process.model.common.PageResult;
import com.example.process.model.flow.FlowDefinition;
import com.example.process.model.flow.FlowVersion;
import com.example.process.model.flow.constant.FlowStatus;
import com.example.process.model.flow.dto.FlowVersionDTO;
import com.example.process.repository.FlowDefinitionRepository;
import com.example.process.repository.FlowVersionRepository;
import com.example.process.service.FlowVersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程版本服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FlowVersionServiceImpl implements FlowVersionService {

    private final FlowVersionRepository flowVersionRepository;
    private final FlowDefinitionRepository flowDefinitionRepository;

    @Override
    @Transactional
    public FlowVersionDTO create(FlowVersionDTO flowVersionDTO) {
        // 检查流程定义是否存在
        FlowDefinition flowDefinition = flowDefinitionRepository.findByIdAndNotDeleted(flowVersionDTO.getFlowDefinitionId())
                .orElseThrow(() -> new BusinessException("流程定义不存在：" + flowVersionDTO.getFlowDefinitionId()));

        // 设置初始状态为草稿
        flowVersionDTO.setStatus(FlowStatus.DRAFT);

        // 设置版本号
        if (flowVersionDTO.getVersion() == null) {
            // 获取最新版本号并加1
            Integer latestVersion = flowVersionRepository.findLatestVersionNumberByFlowDefinitionId(flowVersionDTO.getFlowDefinitionId());
            flowVersionDTO.setVersion(latestVersion != null ? latestVersion + 1 : 1);
        }

        // 保存流程版本
        FlowVersion flowVersion = flowVersionDTO.toEntity();
        FlowVersion savedFlowVersion = flowVersionRepository.save(flowVersion);

        log.info("创建流程版本成功：id={}, flowDefinitionId={}, version={}", 
                savedFlowVersion.getId(), savedFlowVersion.getFlowDefinitionId(), savedFlowVersion.getVersion());
        
        return FlowVersionDTO.fromEntity(savedFlowVersion);
    }

    @Override
    @Transactional
    public FlowVersionDTO update(Long id, FlowVersionDTO flowVersionDTO) {
        // 检查流程版本是否存在
        FlowVersion flowVersion = flowVersionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("流程版本不存在：" + id));

        // 检查是否为草稿状态，只有草稿状态才能更新
        if (!FlowStatus.DRAFT.equals(flowVersion.getStatus())) {
            throw new BusinessException("只有草稿状态的流程版本才能更新");
        }

        // 更新流程版本
        flowVersion.setDescription(flowVersionDTO.getDescription());
        
        // 如果提供了流程图数据，则更新
        if (flowVersionDTO.getFlowGraph() != null) {
            flowVersion.setFlowData(flowVersionDTO.toEntity().getFlowData());
        } else if (flowVersionDTO.getFlowData() != null && !flowVersionDTO.getFlowData().isEmpty()) {
            flowVersion.setFlowData(flowVersionDTO.getFlowData());
        }

        // 保存更新
        FlowVersion updatedFlowVersion = flowVersionRepository.save(flowVersion);

        log.info("更新流程版本成功：id={}, flowDefinitionId={}, version={}", 
                updatedFlowVersion.getId(), updatedFlowVersion.getFlowDefinitionId(), updatedFlowVersion.getVersion());
        
        return FlowVersionDTO.fromEntity(updatedFlowVersion);
    }

    @Override
    public FlowVersionDTO findById(Long id) {
        FlowVersion flowVersion = flowVersionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("流程版本不存在：" + id));

        return FlowVersionDTO.fromEntity(flowVersion);
    }

    @Override
    public FlowVersionDTO findByFlowDefinitionIdAndVersion(Long flowDefinitionId, Integer version) {
        FlowVersion flowVersion = flowVersionRepository.findByFlowDefinitionIdAndVersionAndNotDeleted(flowDefinitionId, version)
                .orElseThrow(() -> new BusinessException("流程版本不存在：flowDefinitionId=" + flowDefinitionId + ", version=" + version));

        return FlowVersionDTO.fromEntity(flowVersion);
    }

    @Override
    public FlowVersionDTO findCurrentVersionByFlowDefinitionId(Long flowDefinitionId) {
        FlowVersion flowVersion = flowVersionRepository.findCurrentVersionByFlowDefinitionId(flowDefinitionId)
                .orElseThrow(() -> new BusinessException("流程当前版本不存在：flowDefinitionId=" + flowDefinitionId));

        return FlowVersionDTO.fromEntity(flowVersion);
    }

    @Override
    public List<FlowVersionDTO> findAllByFlowDefinitionId(Long flowDefinitionId) {
        List<FlowVersion> flowVersions = flowVersionRepository.findAllByFlowDefinitionIdAndNotDeleted(flowDefinitionId);
        
        return flowVersions.stream()
                .map(FlowVersionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<FlowVersionDTO> findByFlowDefinitionIdAndPage(Long flowDefinitionId, PageRequest pageRequest) {
        Pageable pageable = pageRequest.toSpringPageRequest();
        Page<FlowVersion> page = flowVersionRepository.findAllByFlowDefinitionIdAndNotDeleted(flowDefinitionId, pageable);
        
        return PageResult.from(page, FlowVersionDTO::fromEntity);
    }

    @Override
    @Transactional
    public FlowVersionDTO publish(Long id) {
        // 检查流程版本是否存在
        FlowVersion flowVersion = flowVersionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("流程版本不存在：" + id));

        // 检查是否为草稿状态，只有草稿状态才能发布
        if (!FlowStatus.DRAFT.equals(flowVersion.getStatus())) {
            throw new BusinessException("只有草稿状态的流程版本才能发布");
        }

        // 更新状态为已发布
        flowVersion.setStatus(FlowStatus.PUBLISHED);
        flowVersion.setPublishTime(LocalDateTime.now());
        flowVersion.setPublishBy("System"); // 这里可以从Spring Security上下文中获取当前用户

        // 保存更新
        FlowVersion publishedFlowVersion = flowVersionRepository.save(flowVersion);

        log.info("发布流程版本成功：id={}, flowDefinitionId={}, version={}", 
                publishedFlowVersion.getId(), publishedFlowVersion.getFlowDefinitionId(), publishedFlowVersion.getVersion());
        
        return FlowVersionDTO.fromEntity(publishedFlowVersion);
    }

    @Override
    @Transactional
    public FlowVersionDTO disable(Long id) {
        // 检查流程版本是否存在
        FlowVersion flowVersion = flowVersionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("流程版本不存在：" + id));

        // 更新状态为已禁用
        flowVersion.setStatus(FlowStatus.DISABLED);

        // 保存更新
        FlowVersion disabledFlowVersion = flowVersionRepository.save(flowVersion);

        log.info("禁用流程版本成功：id={}, flowDefinitionId={}, version={}", 
                disabledFlowVersion.getId(), disabledFlowVersion.getFlowDefinitionId(), disabledFlowVersion.getVersion());
        
        return FlowVersionDTO.fromEntity(disabledFlowVersion);
    }

    @Override
    @Transactional
    public FlowVersionDTO setAsCurrent(Long id) {
        // 检查流程版本是否存在
        FlowVersion flowVersion = flowVersionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("流程版本不存在：" + id));

        // 检查是否为已发布状态，只有已发布状态才能设为当前版本
        if (!FlowStatus.PUBLISHED.equals(flowVersion.getStatus())) {
            throw new BusinessException("只有已发布状态的流程版本才能设为当前版本");
        }

        // 获取流程定义
        FlowDefinition flowDefinition = flowDefinitionRepository.findByIdAndNotDeleted(flowVersion.getFlowDefinitionId())
                .orElseThrow(() -> new BusinessException("流程定义不存在：" + flowVersion.getFlowDefinitionId()));

        // 获取当前版本
        flowVersionRepository.findCurrentVersionByFlowDefinitionId(flowVersion.getFlowDefinitionId())
                .ifPresent(currentVersion -> {
                    // 取消当前版本的标记
                    currentVersion.setIsCurrent(false);
                    flowVersionRepository.save(currentVersion);
                });

        // 设置为当前版本
        flowVersion.setIsCurrent(true);
        FlowVersion currentFlowVersion = flowVersionRepository.save(flowVersion);

        // 更新流程定义的当前版本号
        flowDefinition.setCurrentVersion(flowVersion.getVersion());
        flowDefinitionRepository.save(flowDefinition);

        log.info("设置流程版本为当前版本成功：id={}, flowDefinitionId={}, version={}", 
                currentFlowVersion.getId(), currentFlowVersion.getFlowDefinitionId(), currentFlowVersion.getVersion());
        
        return FlowVersionDTO.fromEntity(currentFlowVersion);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 检查流程版本是否存在
        FlowVersion flowVersion = flowVersionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("流程版本不存在：" + id));

        // 检查是否为草稿状态，只有草稿状态才能删除
        if (!FlowStatus.DRAFT.equals(flowVersion.getStatus())) {
            throw new BusinessException("只有草稿状态的流程版本才能删除");
        }

        // 逻辑删除
        flowVersion.setIsDeleted(true);
        flowVersionRepository.save(flowVersion);

        log.info("删除流程版本成功：id={}, flowDefinitionId={}, version={}", 
                flowVersion.getId(), flowVersion.getFlowDefinitionId(), flowVersion.getVersion());
    }

    @Override
    @Transactional
    public FlowVersionDTO createNewVersion(Long flowDefinitionId, FlowVersionDTO flowVersionDTO) {
        // 检查流程定义是否存在
        FlowDefinition flowDefinition = flowDefinitionRepository.findByIdAndNotDeleted(flowDefinitionId)
                .orElseThrow(() -> new BusinessException("流程定义不存在：" + flowDefinitionId));

        // 获取最新版本号并加1
        Integer latestVersion = flowVersionRepository.findLatestVersionNumberByFlowDefinitionId(flowDefinitionId);
        Integer newVersion = latestVersion != null ? latestVersion + 1 : 1;

        // 设置新版本信息
        flowVersionDTO.setFlowDefinitionId(flowDefinitionId);
        flowVersionDTO.setVersion(newVersion);
        flowVersionDTO.setStatus(FlowStatus.DRAFT);
        flowVersionDTO.setIsCurrent(false);

        // 保存新版本
        FlowVersion flowVersion = flowVersionDTO.toEntity();
        FlowVersion savedFlowVersion = flowVersionRepository.save(flowVersion);

        log.info("创建新版本成功：id={}, flowDefinitionId={}, version={}", 
                savedFlowVersion.getId(), savedFlowVersion.getFlowDefinitionId(), savedFlowVersion.getVersion());
        
        return FlowVersionDTO.fromEntity(savedFlowVersion);
    }
} 