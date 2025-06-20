package com.example.process.service.impl;

import com.example.process.exception.BusinessException;
import com.example.process.model.common.PageRequest;
import com.example.process.model.common.PageResult;
import com.example.process.model.flow.FlowDefinition;
import com.example.process.model.flow.constant.FlowStatus;
import com.example.process.model.flow.dto.FlowDefinitionDTO;
import com.example.process.repository.FlowDefinitionRepository;
import com.example.process.repository.FlowVersionRepository;
import com.example.process.service.FlowDefinitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程定义服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FlowDefinitionServiceImpl implements FlowDefinitionService {

    private final FlowDefinitionRepository flowDefinitionRepository;
    private final FlowVersionRepository flowVersionRepository;

    @Override
    @Transactional
    public FlowDefinitionDTO create(FlowDefinitionDTO flowDefinitionDTO) {
        // 检查名称是否已存在
        flowDefinitionRepository.findByNameAndNotDeleted(flowDefinitionDTO.getName())
                .ifPresent(existingFlow -> {
                    throw new BusinessException("流程名称已存在：" + flowDefinitionDTO.getName());
                });

        // 设置初始状态为草稿
        if (!StringUtils.hasText(flowDefinitionDTO.getStatus())) {
            flowDefinitionDTO.setStatus(FlowStatus.DRAFT);
        }

        // 设置初始版本号
        flowDefinitionDTO.setCurrentVersion(1);

        // 保存流程定义
        FlowDefinition flowDefinition = flowDefinitionDTO.toEntity();
        FlowDefinition savedFlowDefinition = flowDefinitionRepository.save(flowDefinition);

        log.info("创建流程定义成功：id={}, name={}", savedFlowDefinition.getId(), savedFlowDefinition.getName());
        return FlowDefinitionDTO.fromEntity(savedFlowDefinition);
    }

    @Override
    @Transactional
    public FlowDefinitionDTO update(Long id, FlowDefinitionDTO flowDefinitionDTO) {
        // 检查流程定义是否存在
        FlowDefinition flowDefinition = flowDefinitionRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new BusinessException("流程定义不存在：" + id));

        // 如果名称发生变化，检查新名称是否已存在
        if (!flowDefinition.getName().equals(flowDefinitionDTO.getName())) {
            flowDefinitionRepository.findByNameAndNotDeleted(flowDefinitionDTO.getName())
                    .ifPresent(existingFlow -> {
                        throw new BusinessException("流程名称已存在：" + flowDefinitionDTO.getName());
                    });
        }

        // 更新流程定义
        flowDefinition.setName(flowDefinitionDTO.getName());
        flowDefinition.setDescription(flowDefinitionDTO.getDescription());
        flowDefinition.setType(flowDefinitionDTO.getType());
        flowDefinition.setTags(flowDefinitionDTO.getTags());
        flowDefinition.setTeamId(flowDefinitionDTO.getTeamId());

        // 保存更新
        FlowDefinition updatedFlowDefinition = flowDefinitionRepository.save(flowDefinition);

        log.info("更新流程定义成功：id={}, name={}", updatedFlowDefinition.getId(), updatedFlowDefinition.getName());
        return FlowDefinitionDTO.fromEntity(updatedFlowDefinition);
    }

    @Override
    public FlowDefinitionDTO findById(Long id) {
        FlowDefinition flowDefinition = flowDefinitionRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new BusinessException("流程定义不存在：" + id));

        return FlowDefinitionDTO.fromEntity(flowDefinition);
    }

    @Override
    public PageResult<FlowDefinitionDTO> findByPage(PageRequest pageRequest, String type, String status, String name, String tag, Long teamId) {
        Specification<FlowDefinition> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 只查询未删除的记录
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));

            // 添加类型条件
            if (StringUtils.hasText(type)) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }

            // 添加状态条件
            if (StringUtils.hasText(status)) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // 添加名称模糊查询条件
            if (StringUtils.hasText(name)) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }

            // 添加标签条件
            if (StringUtils.hasText(tag)) {
                predicates.add(criteriaBuilder.like(root.get("tags"), "%" + tag + "%"));
            }

            // 添加团队ID条件
            if (teamId != null) {
                predicates.add(criteriaBuilder.equal(root.get("teamId"), teamId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = pageRequest.toSpringPageRequest();
        Page<FlowDefinition> page = flowDefinitionRepository.findAll(spec, pageable);

        return PageResult.from(page, FlowDefinitionDTO::fromEntity);
    }

    @Override
    public List<FlowDefinitionDTO> findByType(String type) {
        List<FlowDefinition> flowDefinitions = flowDefinitionRepository.findByTypeAndNotDeleted(type);
        return flowDefinitions.stream()
                .map(FlowDefinitionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowDefinitionDTO> findByStatus(String status) {
        List<FlowDefinition> flowDefinitions = flowDefinitionRepository.findByStatusAndNotDeleted(status);
        return flowDefinitions.stream()
                .map(FlowDefinitionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowDefinitionDTO> findByTag(String tag) {
        List<FlowDefinition> flowDefinitions = flowDefinitionRepository.findByTagAndNotDeleted(tag);
        return flowDefinitions.stream()
                .map(FlowDefinitionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowDefinitionDTO> findByTeamId(Long teamId) {
        List<FlowDefinition> flowDefinitions = flowDefinitionRepository.findByTeamIdAndNotDeleted(teamId);
        return flowDefinitions.stream()
                .map(FlowDefinitionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        FlowDefinition flowDefinition = flowDefinitionRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new BusinessException("流程定义不存在：" + id));

        // 逻辑删除
        flowDefinition.setIsDeleted(true);
        flowDefinitionRepository.save(flowDefinition);

        log.info("删除流程定义成功：id={}, name={}", flowDefinition.getId(), flowDefinition.getName());
    }

    @Override
    @Transactional
    public FlowDefinitionDTO updateStatus(Long id, String status) {
        FlowDefinition flowDefinition = flowDefinitionRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new BusinessException("流程定义不存在：" + id));

        // 更新状态
        flowDefinition.setStatus(status);
        FlowDefinition updatedFlowDefinition = flowDefinitionRepository.save(flowDefinition);

        log.info("更新流程定义状态成功：id={}, name={}, status={}", 
                updatedFlowDefinition.getId(), updatedFlowDefinition.getName(), updatedFlowDefinition.getStatus());
        
        return FlowDefinitionDTO.fromEntity(updatedFlowDefinition);
    }
} 