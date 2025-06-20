package com.example.process.controller;

import com.example.process.model.common.ApiResponse;
import com.example.process.model.common.PageRequest;
import com.example.process.model.common.PageResult;
import com.example.process.model.flow.dto.FlowDefinitionDTO;
import com.example.process.service.FlowDefinitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程定义控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/flow/definitions")
@RequiredArgsConstructor
public class FlowDefinitionController {

    private final FlowDefinitionService flowDefinitionService;

    /**
     * 创建流程定义
     *
     * @param flowDefinitionDTO 流程定义DTO
     * @return API响应
     */
    @PostMapping
    public ApiResponse<FlowDefinitionDTO> create(@RequestBody FlowDefinitionDTO flowDefinitionDTO) {
        log.info("创建流程定义：{}", flowDefinitionDTO);
        FlowDefinitionDTO createdFlowDefinition = flowDefinitionService.create(flowDefinitionDTO);
        return ApiResponse.success("创建流程定义成功", createdFlowDefinition);
    }

    /**
     * 更新流程定义
     *
     * @param id               流程定义ID
     * @param flowDefinitionDTO 流程定义DTO
     * @return API响应
     */
    @PutMapping("/{id}")
    public ApiResponse<FlowDefinitionDTO> update(@PathVariable Long id, @RequestBody FlowDefinitionDTO flowDefinitionDTO) {
        log.info("更新流程定义：id={}, flowDefinition={}", id, flowDefinitionDTO);
        FlowDefinitionDTO updatedFlowDefinition = flowDefinitionService.update(id, flowDefinitionDTO);
        return ApiResponse.success("更新流程定义成功", updatedFlowDefinition);
    }

    /**
     * 根据ID查询流程定义
     *
     * @param id 流程定义ID
     * @return API响应
     */
    @GetMapping("/{id}")
    public ApiResponse<FlowDefinitionDTO> findById(@PathVariable Long id) {
        log.info("查询流程定义：id={}", id);
        FlowDefinitionDTO flowDefinition = flowDefinitionService.findById(id);
        return ApiResponse.success("查询流程定义成功", flowDefinition);
    }

    /**
     * 分页查询流程定义
     *
     * @param pageRequest 分页参数
     * @param type        流程类型（可选）
     * @param status      流程状态（可选）
     * @param name        流程名称（可选，模糊查询）
     * @param tag         标签（可选）
     * @param teamId      团队ID（可选）
     * @return API响应
     */
    @GetMapping
    public ApiResponse<PageResult<FlowDefinitionDTO>> findByPage(
            PageRequest pageRequest,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) Long teamId) {
        
        log.info("分页查询流程定义：pageRequest={}, type={}, status={}, name={}, tag={}, teamId={}",
                pageRequest, type, status, name, tag, teamId);
        
        PageResult<FlowDefinitionDTO> pageResult = flowDefinitionService.findByPage(
                pageRequest, type, status, name, tag, teamId);
        
        return ApiResponse.success("查询流程定义成功", pageResult);
    }

    /**
     * 根据类型查询流程定义
     *
     * @param type 流程类型
     * @return API响应
     */
    @GetMapping("/by-type/{type}")
    public ApiResponse<List<FlowDefinitionDTO>> findByType(@PathVariable String type) {
        log.info("根据类型查询流程定义：type={}", type);
        List<FlowDefinitionDTO> flowDefinitions = flowDefinitionService.findByType(type);
        return ApiResponse.success("查询流程定义成功", flowDefinitions);
    }

    /**
     * 根据状态查询流程定义
     *
     * @param status 流程状态
     * @return API响应
     */
    @GetMapping("/by-status/{status}")
    public ApiResponse<List<FlowDefinitionDTO>> findByStatus(@PathVariable String status) {
        log.info("根据状态查询流程定义：status={}", status);
        List<FlowDefinitionDTO> flowDefinitions = flowDefinitionService.findByStatus(status);
        return ApiResponse.success("查询流程定义成功", flowDefinitions);
    }

    /**
     * 根据标签查询流程定义
     *
     * @param tag 标签
     * @return API响应
     */
    @GetMapping("/by-tag/{tag}")
    public ApiResponse<List<FlowDefinitionDTO>> findByTag(@PathVariable String tag) {
        log.info("根据标签查询流程定义：tag={}", tag);
        List<FlowDefinitionDTO> flowDefinitions = flowDefinitionService.findByTag(tag);
        return ApiResponse.success("查询流程定义成功", flowDefinitions);
    }

    /**
     * 根据团队ID查询流程定义
     *
     * @param teamId 团队ID
     * @return API响应
     */
    @GetMapping("/by-team/{teamId}")
    public ApiResponse<List<FlowDefinitionDTO>> findByTeamId(@PathVariable Long teamId) {
        log.info("根据团队ID查询流程定义：teamId={}", teamId);
        List<FlowDefinitionDTO> flowDefinitions = flowDefinitionService.findByTeamId(teamId);
        return ApiResponse.success("查询流程定义成功", flowDefinitions);
    }

    /**
     * 删除流程定义
     *
     * @param id 流程定义ID
     * @return API响应
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        log.info("删除流程定义：id={}", id);
        flowDefinitionService.delete(id);
        return ApiResponse.success("删除流程定义成功", null);
    }

    /**
     * 更新流程定义状态
     *
     * @param id     流程定义ID
     * @param status 新状态
     * @return API响应
     */
    @PutMapping("/{id}/status")
    public ApiResponse<FlowDefinitionDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        
        log.info("更新流程定义状态：id={}, status={}", id, status);
        FlowDefinitionDTO updatedFlowDefinition = flowDefinitionService.updateStatus(id, status);
        return ApiResponse.success("更新流程定义状态成功", updatedFlowDefinition);
    }
} 