package com.example.process.repository;

import com.example.process.model.flow.FlowDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 流程定义数据访问层
 */
@Repository
public interface FlowDefinitionRepository extends JpaRepository<FlowDefinition, Long>, JpaSpecificationExecutor<FlowDefinition> {

    /**
     * 根据ID查询未删除的流程定义
     *
     * @param id 流程定义ID
     * @return 流程定义
     */
    @Query("SELECT f FROM FlowDefinition f WHERE f.id = :id AND f.isDeleted = false")
    Optional<FlowDefinition> findByIdAndNotDeleted(@Param("id") Long id);

    /**
     * 根据名称查询未删除的流程定义
     *
     * @param name 流程定义名称
     * @return 流程定义
     */
    @Query("SELECT f FROM FlowDefinition f WHERE f.name = :name AND f.isDeleted = false")
    Optional<FlowDefinition> findByNameAndNotDeleted(@Param("name") String name);

    /**
     * 分页查询未删除的流程定义
     *
     * @param pageable 分页参数
     * @return 流程定义分页结果
     */
    @Query("SELECT f FROM FlowDefinition f WHERE f.isDeleted = false")
    Page<FlowDefinition> findAllNotDeleted(Pageable pageable);

    /**
     * 根据类型查询未删除的流程定义
     *
     * @param type 流程类型
     * @return 流程定义列表
     */
    @Query("SELECT f FROM FlowDefinition f WHERE f.type = :type AND f.isDeleted = false")
    List<FlowDefinition> findByTypeAndNotDeleted(@Param("type") String type);

    /**
     * 根据状态查询未删除的流程定义
     *
     * @param status 状态
     * @return 流程定义列表
     */
    @Query("SELECT f FROM FlowDefinition f WHERE f.status = :status AND f.isDeleted = false")
    List<FlowDefinition> findByStatusAndNotDeleted(@Param("status") String status);

    /**
     * 根据标签查询未删除的流程定义
     *
     * @param tag 标签
     * @return 流程定义列表
     */
    @Query("SELECT f FROM FlowDefinition f WHERE f.tags LIKE %:tag% AND f.isDeleted = false")
    List<FlowDefinition> findByTagAndNotDeleted(@Param("tag") String tag);

    /**
     * 根据团队ID查询未删除的流程定义
     *
     * @param teamId 团队ID
     * @return 流程定义列表
     */
    @Query("SELECT f FROM FlowDefinition f WHERE f.teamId = :teamId AND f.isDeleted = false")
    List<FlowDefinition> findByTeamIdAndNotDeleted(@Param("teamId") Long teamId);
} 