package com.example.process.repository;

import com.example.process.model.engine.FlowInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 流程实例数据访问接口
 */
@Repository
public interface FlowInstanceRepository extends JpaRepository<FlowInstance, Long> {

    /**
     * 根据流程定义ID查询流程实例列表
     *
     * @param flowDefinitionId 流程定义ID
     * @return 流程实例列表
     */
    List<FlowInstance> findByFlowDefinitionId(Long flowDefinitionId);

    /**
     * 根据流程定义ID分页查询流程实例
     *
     * @param flowDefinitionId 流程定义ID
     * @param pageable        分页参数
     * @return 流程实例分页结果
     */
    Page<FlowInstance> findByFlowDefinitionId(Long flowDefinitionId, Pageable pageable);

    /**
     * 根据流程版本ID查询流程实例列表
     *
     * @param flowVersionId 流程版本ID
     * @return 流程实例列表
     */
    List<FlowInstance> findByFlowVersionId(Long flowVersionId);

    /**
     * 根据流程版本ID分页查询流程实例
     *
     * @param flowVersionId 流程版本ID
     * @param pageable     分页参数
     * @return 流程实例分页结果
     */
    Page<FlowInstance> findByFlowVersionId(Long flowVersionId, Pageable pageable);

    /**
     * 根据状态查询流程实例列表
     *
     * @param status 状态
     * @return 流程实例列表
     */
    List<FlowInstance> findByStatus(String status);

    /**
     * 根据状态分页查询流程实例
     *
     * @param status   状态
     * @param pageable 分页参数
     * @return 流程实例分页结果
     */
    Page<FlowInstance> findByStatus(String status, Pageable pageable);

    /**
     * 根据创建者ID查询流程实例列表
     *
     * @param creatorId 创建者ID
     * @return 流程实例列表
     */
    List<FlowInstance> findByCreatorId(Long creatorId);

    /**
     * 根据创建者ID分页查询流程实例
     *
     * @param creatorId 创建者ID
     * @param pageable  分页参数
     * @return 流程实例分页结果
     */
    Page<FlowInstance> findByCreatorId(Long creatorId, Pageable pageable);

    /**
     * 根据团队ID查询流程实例列表
     *
     * @param teamId 团队ID
     * @return 流程实例列表
     */
    List<FlowInstance> findByTeamId(Long teamId);

    /**
     * 根据团队ID分页查询流程实例
     *
     * @param teamId   团队ID
     * @param pageable 分页参数
     * @return 流程实例分页结果
     */
    Page<FlowInstance> findByTeamId(Long teamId, Pageable pageable);

    /**
     * 根据父流程实例ID查询子流程实例列表
     *
     * @param parentInstanceId 父流程实例ID
     * @return 子流程实例列表
     */
    List<FlowInstance> findByParentInstanceId(Long parentInstanceId);

    /**
     * 根据名称模糊查询流程实例
     *
     * @param name     名称关键字
     * @param pageable 分页参数
     * @return 流程实例分页结果
     */
    Page<FlowInstance> findByNameContaining(String name, Pageable pageable);

    /**
     * 根据标签查询流程实例
     *
     * @param tag      标签关键字
     * @param pageable 分页参数
     * @return 流程实例分页结果
     */
    @Query("SELECT i FROM FlowInstance i WHERE i.tags LIKE %:tag%")
    Page<FlowInstance> findByTag(@Param("tag") String tag, Pageable pageable);

    /**
     * 组合条件查询流程实例
     *
     * @param flowDefinitionId 流程定义ID（可选）
     * @param status          状态（可选）
     * @param creatorId       创建者ID（可选）
     * @param teamId          团队ID（可选）
     * @param pageable        分页参数
     * @return 流程实例分页结果
     */
    @Query("SELECT i FROM FlowInstance i WHERE " +
            "(:flowDefinitionId IS NULL OR i.flowDefinitionId = :flowDefinitionId) AND " +
            "(:status IS NULL OR i.status = :status) AND " +
            "(:creatorId IS NULL OR i.creatorId = :creatorId) AND " +
            "(:teamId IS NULL OR i.teamId = :teamId)")
    Page<FlowInstance> findByConditions(
            @Param("flowDefinitionId") Long flowDefinitionId,
            @Param("status") String status,
            @Param("creatorId") Long creatorId,
            @Param("teamId") Long teamId,
            Pageable pageable);

    /**
     * 统计指定状态的流程实例数量
     *
     * @param status 状态
     * @return 数量
     */
    long countByStatus(String status);
} 