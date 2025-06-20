package com.example.process.controller;

import com.example.process.model.common.ApiResponse;
import com.example.process.model.common.PageRequest;
import com.example.process.model.common.PageResult;
import com.example.process.model.flow.dto.FlowVersionDTO;
import com.example.process.service.FlowVersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程版本控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/flow/versions")
@RequiredArgsConstructor
public class FlowVersionController {

    private final FlowVersionService flowVersionService;

    /**
     * 创建流程版本
     *
     * @param flowVersionDTO 流程版本DTO
     * @return API响应
     */
    @PostMapping
    public ApiResponse<FlowVersionDTO> create(@RequestBody FlowVersionDTO flowVersionDTO) {
        log.info("创建流程版本：{}", flowVersionDTO);
        FlowVersionDTO createdFlowVersion = flowVersionService.create(flowVersionDTO);
        return ApiResponse.success("创建流程版本成功", createdFlowVersion);
    }

    /**
     * 更新流程版本
     *
     * @param id            流程版本ID
     * @param flowVersionDTO 流程版本DTO
     * @return API响应
     */
    @PutMapping("/{id}")
    public ApiResponse<FlowVersionDTO> update(@PathVariable Long id, @RequestBody FlowVersionDTO flowVersionDTO) {
        log.info("更新流程版本：id={}, flowVersion={}", id, flowVersionDTO);
        FlowVersionDTO updatedFlowVersion = flowVersionService.update(id, flowVersionDTO);
        return ApiResponse.success("更新流程版本成功", updatedFlowVersion);
    }

    /**
     * 根据ID查询流程版本
     *
     * @param id 流程版本ID
     * @return API响应
     */
    @GetMapping("/{id}")
    public ApiResponse<FlowVersionDTO> findById(@PathVariable Long id) {
        log.info("查询流程版本：id={}", id);
        FlowVersionDTO flowVersion = flowVersionService.findById(id);
        return ApiResponse.success("查询流程版本成功", flowVersion);
    }

    /**
     * 根据流程定义ID和版本号查询流程版本
     *
     * @param flowDefinitionId 流程定义ID
     * @param version         版本号
     * @return API响应
     */
    @GetMapping("/by-definition/{flowDefinitionId}/version/{version}")
    public ApiResponse<FlowVersionDTO> findByFlowDefinitionIdAndVersion(
            @PathVariable Long flowDefinitionId,
            @PathVariable Integer version) {
        
        log.info("根据流程定义ID和版本号查询流程版本：flowDefinitionId={}, version={}", flowDefinitionId, version);
        FlowVersionDTO flowVersion = flowVersionService.findByFlowDefinitionIdAndVersion(flowDefinitionId, version);
        return ApiResponse.success("查询流程版本成功", flowVersion);
    }

    /**
     * 根据流程定义ID查询当前版本
     *
     * @param flowDefinitionId 流程定义ID
     * @return API响应
     */
    @GetMapping("/by-definition/{flowDefinitionId}/current")
    public ApiResponse<FlowVersionDTO> findCurrentVersionByFlowDefinitionId(@PathVariable Long flowDefinitionId) {
        log.info("根据流程定义ID查询当前版本：flowDefinitionId={}", flowDefinitionId);
        FlowVersionDTO flowVersion = flowVersionService.findCurrentVersionByFlowDefinitionId(flowDefinitionId);
        return ApiResponse.success("查询流程当前版本成功", flowVersion);
    }

    /**
     * 根据流程定义ID查询所有版本
     *
     * @param flowDefinitionId 流程定义ID
     * @return API响应
     */
    @GetMapping("/by-definition/{flowDefinitionId}")
    public ApiResponse<List<FlowVersionDTO>> findAllByFlowDefinitionId(@PathVariable Long flowDefinitionId) {
        log.info("根据流程定义ID查询所有版本：flowDefinitionId={}", flowDefinitionId);
        List<FlowVersionDTO> flowVersions = flowVersionService.findAllByFlowDefinitionId(flowDefinitionId);
        return ApiResponse.success("查询流程版本列表成功", flowVersions);
    }

    /**
     * 根据流程定义ID分页查询版本
     *
     * @param flowDefinitionId 流程定义ID
     * @param pageRequest     分页参数
     * @return API响应
     */
    @GetMapping("/by-definition/{flowDefinitionId}/page")
    public ApiResponse<PageResult<FlowVersionDTO>> findByFlowDefinitionIdAndPage(
            @PathVariable Long flowDefinitionId,
            PageRequest pageRequest) {
        
        log.info("根据流程定义ID分页查询版本：flowDefinitionId={}, pageRequest={}", flowDefinitionId, pageRequest);
        PageResult<FlowVersionDTO> pageResult = flowVersionService.findByFlowDefinitionIdAndPage(flowDefinitionId, pageRequest);
        return ApiResponse.success("查询流程版本分页成功", pageResult);
    }

    /**
     * 发布流程版本
     *
     * @param id 流程版本ID
     * @return API响应
     */
    @PutMapping("/{id}/publish")
    public ApiResponse<FlowVersionDTO> publish(@PathVariable Long id) {
        log.info("发布流程版本：id={}", id);
        FlowVersionDTO publishedFlowVersion = flowVersionService.publish(id);
        return ApiResponse.success("发布流程版本成功", publishedFlowVersion);
    }

    /**
     * 禁用流程版本
     *
     * @param id 流程版本ID
     * @return API响应
     */
    @PutMapping("/{id}/disable")
    public ApiResponse<FlowVersionDTO> disable(@PathVariable Long id) {
        log.info("禁用流程版本：id={}", id);
        FlowVersionDTO disabledFlowVersion = flowVersionService.disable(id);
        return ApiResponse.success("禁用流程版本成功", disabledFlowVersion);
    }

    /**
     * 设置为当前版本
     *
     * @param id 流程版本ID
     * @return API响应
     */
    @PutMapping("/{id}/set-as-current")
    public ApiResponse<FlowVersionDTO> setAsCurrent(@PathVariable Long id) {
        log.info("设置为当前版本：id={}", id);
        FlowVersionDTO currentFlowVersion = flowVersionService.setAsCurrent(id);
        return ApiResponse.success("设置当前版本成功", currentFlowVersion);
    }

    /**
     * 删除流程版本
     *
     * @param id 流程版本ID
     * @return API响应
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        log.info("删除流程版本：id={}", id);
        flowVersionService.delete(id);
        return ApiResponse.success("删除流程版本成功", null);
    }

    /**
     * 创建新版本
     *
     * @param flowDefinitionId 流程定义ID
     * @param flowVersionDTO   流程版本DTO
     * @return API响应
     */
    @PostMapping("/by-definition/{flowDefinitionId}/new-version")
    public ApiResponse<FlowVersionDTO> createNewVersion(
            @PathVariable Long flowDefinitionId,
            @RequestBody FlowVersionDTO flowVersionDTO) {
        
        log.info("创建新版本：flowDefinitionId={}, flowVersionDTO={}", flowDefinitionId, flowVersionDTO);
        FlowVersionDTO newFlowVersion = flowVersionService.createNewVersion(flowDefinitionId, flowVersionDTO);
        return ApiResponse.success("创建新版本成功", newFlowVersion);
    }
} 