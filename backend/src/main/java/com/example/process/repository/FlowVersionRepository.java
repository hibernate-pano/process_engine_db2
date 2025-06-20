package com.example.process.repository;

import com.example.process.model.flow.FlowVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 流程版本数据访问层
 */
@Repository
public interface FlowVersionRepository extends JpaRepository<FlowVersion, Long> {

    /**
     * 根据流程定义ID查询流程版本列表
     *
     * @param flowDefinitionId 流程定义ID
     * @return 流程版本列表
     */
    List<FlowVersion> findByFlowDefinitionId(Long flowDefinitionId);

    /**
     * 根据流程定义ID分页查询流程版本
     *
     * @param flowDefinitionId 流程定义ID
     * @param pageable        分页参数
     * @return 流程版本分页结果
     */
    Page<FlowVersion> findByFlowDefinitionId(Long flowDefinitionId, Pageable pageable);

    /**
     * 根据流程定义ID和版本号查询流程版本
     *
     * @param flowDefinitionId 流程定义ID
     * @param version         版本号
     * @return 流程版本
     */
    Optional<FlowVersion> findByFlowDefinitionIdAndVersion(Long flowDefinitionId, Integer version);

    /**
     * 根据流程定义ID查询最新版本
     *
     * @param flowDefinitionId 流程定义ID
     * @return 最新版本
     */
    Optional<FlowVersion> findTopByFlowDefinitionIdOrderByVersionDesc(Long flowDefinitionId);

    /**
     * 根据流程定义ID和版本号查询未删除的流程版本
     *
     * @param flowDefinitionId 流程定义ID
     * @param version         版本号
     * @return 流程版本
     */
    @Query("SELECT v FROM FlowVersion v WHERE v.flowDefinitionId = :flowDefinitionId AND v.version = :version AND v.isDeleted = false")
    Optional<FlowVersion> findByFlowDefinitionIdAndVersionAndNotDeleted(
            @Param("flowDefinitionId") Long flowDefinitionId,
            @Param("version") Integer version);

    /**
     * 根据流程定义ID查询当前版本
     *
     * @param flowDefinitionId 流程定义ID
     * @return 流程版本
     */
    @Query("SELECT v FROM FlowVersion v WHERE v.flowDefinitionId = :flowDefinitionId AND v.isCurrent = true AND v.isDeleted = false")
    Optional<FlowVersion> findCurrentVersionByFlowDefinitionId(@Param("flowDefinitionId") Long flowDefinitionId);

    /**
     * 根据流程定义ID查询所有未删除的流程版本
     *
     * @param flowDefinitionId 流程定义ID
     * @return 流程版本列表
     */
    @Query("SELECT v FROM FlowVersion v WHERE v.flowDefinitionId = :flowDefinitionId AND v.isDeleted = false ORDER BY v.version DESC")
    List<FlowVersion> findAllByFlowDefinitionIdAndNotDeleted(@Param("flowDefinitionId") Long flowDefinitionId);

    /**
     * 根据流程定义ID分页查询未删除的流程版本
     *
     * @param flowDefinitionId 流程定义ID
     * @param pageable        分页参数
     * @return 流程版本分页结果
     */
    @Query("SELECT v FROM FlowVersion v WHERE v.flowDefinitionId = :flowDefinitionId AND v.isDeleted = false ORDER BY v.version DESC")
    Page<FlowVersion> findAllByFlowDefinitionIdAndNotDeleted(
            @Param("flowDefinitionId") Long flowDefinitionId,
            Pageable pageable);

    /**
     * 根据流程定义ID和状态查询未删除的流程版本
     *
     * @param flowDefinitionId 流程定义ID
     * @param status          状态
     * @return 流程版本列表
     */
    @Query("SELECT v FROM FlowVersion v WHERE v.flowDefinitionId = :flowDefinitionId AND v.status = :status AND v.isDeleted = false ORDER BY v.version DESC")
    List<FlowVersion> findByFlowDefinitionIdAndStatusAndNotDeleted(
            @Param("flowDefinitionId") Long flowDefinitionId,
            @Param("status") String status);

    /**
     * 获取流程定义的最新版本号
     *
     * @param flowDefinitionId 流程定义ID
     * @return 最新版本号
     */
    @Query("SELECT MAX(v.version) FROM FlowVersion v WHERE v.flowDefinitionId = :flowDefinitionId AND v.isDeleted = false")
    Integer findLatestVersionNumberByFlowDefinitionId(@Param("flowDefinitionId") Long flowDefinitionId);
} 