package com.example.process.controller;

import com.example.process.model.common.PageRequest;
import com.example.process.model.common.PageResult;
import com.example.process.model.engine.dto.FlowInstanceDTO;
import com.example.process.service.FlowInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 流程实例控制器
 */
@RestController
@RequestMapping("/api/flow-instances")
public class FlowInstanceController {

    private final FlowInstanceService flowInstanceService;

    @Autowired
    public FlowInstanceController(FlowInstanceService flowInstanceService) {
        this.flowInstanceService = flowInstanceService;
    }

    /**
     * 创建流程实例
     *
     * @param request 创建请求
     * @return 创建的流程实例
     */
    @PostMapping
    public ResponseEntity<FlowInstanceDTO> createInstance(@RequestBody Map<String, Object> request) {
        Long flowDefinitionId = Long.valueOf(request.get("flowDefinitionId").toString());
        Long flowVersionId = request.get("flowVersionId") != null ? 
                Long.valueOf(request.get("flowVersionId").toString()) : null;
        String name = (String) request.get("name");
        String description = (String) request.get("description");
        Map<String, Object> variables = (Map<String, Object>) request.get("variables");
        Long creatorId = request.get("creatorId") != null ? 
                Long.valueOf(request.get("creatorId").toString()) : null;
        Long teamId = request.get("teamId") != null ? 
                Long.valueOf(request.get("teamId").toString()) : null;
        String priority = (String) request.get("priority");
        List<String> tags = (List<String>) request.get("tags");
        Long parentInstanceId = request.get("parentInstanceId") != null ? 
                Long.valueOf(request.get("parentInstanceId").toString()) : null;

        FlowInstanceDTO instance = flowInstanceService.createInstance(
                flowDefinitionId, flowVersionId, name, description, variables,
                creatorId, teamId, priority, tags, parentInstanceId);

        return ResponseEntity.ok(instance);
    }

    /**
     * 启动流程实例
     *
     * @param id 实例ID
     * @return 启动后的流程实例
     */
    @PostMapping("/{id}/start")
    public ResponseEntity<FlowInstanceDTO> startInstance(@PathVariable Long id) {
        FlowInstanceDTO instance = flowInstanceService.startInstance(id);
        return ResponseEntity.ok(instance);
    }

    /**
     * 暂停流程实例
     *
     * @param id 实例ID
     * @return 暂停后的流程实例
     */
    @PostMapping("/{id}/suspend")
    public ResponseEntity<FlowInstanceDTO> suspendInstance(@PathVariable Long id) {
        FlowInstanceDTO instance = flowInstanceService.suspendInstance(id);
        return ResponseEntity.ok(instance);
    }

    /**
     * 恢复流程实例
     *
     * @param id 实例ID
     * @return 恢复后的流程实例
     */
    @PostMapping("/{id}/resume")
    public ResponseEntity<FlowInstanceDTO> resumeInstance(@PathVariable Long id) {
        FlowInstanceDTO instance = flowInstanceService.resumeInstance(id);
        return ResponseEntity.ok(instance);
    }

    /**
     * 取消流程实例
     *
     * @param id 实例ID
     * @return 取消后的流程实例
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<FlowInstanceDTO> cancelInstance(@PathVariable Long id) {
        FlowInstanceDTO instance = flowInstanceService.cancelInstance(id);
        return ResponseEntity.ok(instance);
    }

    /**
     * 获取流程实例
     *
     * @param id 实例ID
     * @return 流程实例
     */
    @GetMapping("/{id}")
    public ResponseEntity<FlowInstanceDTO> getInstance(@PathVariable Long id) {
        FlowInstanceDTO instance = flowInstanceService.getInstance(id);
        return ResponseEntity.ok(instance);
    }

    /**
     * 更新流程实例
     *
     * @param id      实例ID
     * @param request 更新请求
     * @return 更新后的流程实例
     */
    @PutMapping("/{id}")
    public ResponseEntity<FlowInstanceDTO> updateInstance(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        String description = (String) request.get("description");
        String priority = (String) request.get("priority");
        List<String> tags = (List<String>) request.get("tags");

        FlowInstanceDTO instance = flowInstanceService.updateInstance(
                id, name, description, priority, tags);

        return ResponseEntity.ok(instance);
    }

    /**
     * 更新流程实例变量
     *
     * @param id        实例ID
     * @param variables 变量
     * @return 更新后的流程实例
     */
    @PutMapping("/{id}/variables")
    public ResponseEntity<FlowInstanceDTO> updateVariables(
            @PathVariable Long id,
            @RequestBody Map<String, Object> variables) {
        FlowInstanceDTO instance = flowInstanceService.updateVariables(id, variables);
        return ResponseEntity.ok(instance);
    }

    /**
     * 获取流程实例变量
     *
     * @param id 实例ID
     * @return 变量
     */
    @GetMapping("/{id}/variables")
    public ResponseEntity<Map<String, Object>> getVariables(@PathVariable Long id) {
        Map<String, Object> variables = flowInstanceService.getInstanceVariables(id);
        return ResponseEntity.ok(variables);
    }

    /**
     * 获取流程实例活动节点
     *
     * @param id 实例ID
     * @return 活动节点ID列表
     */
    @GetMapping("/{id}/active-nodes")
    public ResponseEntity<List<String>> getActiveNodes(@PathVariable Long id) {
        List<String> activeNodes = flowInstanceService.getActiveNodes(id);
        return ResponseEntity.ok(activeNodes);
    }

    /**
     * 分页查询流程实例
     *
     * @param pageNum          页码
     * @param pageSize         每页大小
     * @param flowDefinitionId 流程定义ID（可选）
     * @param status           状态（可选）
     * @param creatorId        创建者ID（可选）
     * @param teamId           团队ID（可选）
     * @param name             名称关键字（可选）
     * @param tag              标签关键字（可选）
     * @return 流程实例分页结果
     */
    @GetMapping
    public ResponseEntity<PageResult<FlowInstanceDTO>> listInstances(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long flowDefinitionId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long creatorId,
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String tag) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        PageResult<FlowInstanceDTO> result = flowInstanceService.listInstances(
                pageRequest, flowDefinitionId, status, creatorId, teamId, name, tag);
        return ResponseEntity.ok(result);
    }

    /**
     * 查询流程定义的所有实例
     *
     * @param definitionId 流程定义ID
     * @return 流程实例列表
     */
    @GetMapping("/by-definition/{definitionId}")
    public ResponseEntity<List<FlowInstanceDTO>> listInstancesByDefinition(
            @PathVariable Long definitionId) {
        List<FlowInstanceDTO> instances = flowInstanceService.listInstancesByDefinition(definitionId);
        return ResponseEntity.ok(instances);
    }

    /**
     * 查询流程版本的所有实例
     *
     * @param versionId 流程版本ID
     * @return 流程实例列表
     */
    @GetMapping("/by-version/{versionId}")
    public ResponseEntity<List<FlowInstanceDTO>> listInstancesByVersion(
            @PathVariable Long versionId) {
        List<FlowInstanceDTO> instances = flowInstanceService.listInstancesByVersion(versionId);
        return ResponseEntity.ok(instances);
    }

    /**
     * 查询子流程实例
     *
     * @param parentId 父流程实例ID
     * @return 子流程实例列表
     */
    @GetMapping("/children/{parentId}")
    public ResponseEntity<List<FlowInstanceDTO>> listChildInstances(
            @PathVariable Long parentId) {
        List<FlowInstanceDTO> instances = flowInstanceService.listChildInstances(parentId);
        return ResponseEntity.ok(instances);
    }

    /**
     * 删除流程实例
     *
     * @param id 实例ID
     * @return 无内容响应
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstance(@PathVariable Long id) {
        flowInstanceService.deleteInstance(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取流程实例状态统计
     *
     * @return 状态统计结果
     */
    @GetMapping("/statistics/status")
    public ResponseEntity<Map<String, Long>> getStatusStatistics() {
        Map<String, Long> statistics = flowInstanceService.getInstanceStatusStatistics();
        return ResponseEntity.ok(statistics);
    }
} 