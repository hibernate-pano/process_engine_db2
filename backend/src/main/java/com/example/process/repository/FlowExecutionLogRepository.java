package com.example.process.repository;

import com.example.process.model.engine.FlowExecutionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 流程执行记录数据访问接口
 */
@Repository
public interface FlowExecutionLogRepository extends JpaRepository<FlowExecutionLog, Long> {

    /**
     * 根据流程实例ID查询执行记录
     *
     * @param flowInstanceId 流程实例ID
     * @return 执行记录列表
     */
    List<FlowExecutionLog> findByFlowInstanceId(Long flowInstanceId);

    /**
     * 根据流程实例ID分页查询执行记录
     *
     * @param flowInstanceId 流程实例ID
     * @param pageable      分页参数
     * @return 执行记录分页结果
     */
    Page<FlowExecutionLog> findByFlowInstanceId(Long flowInstanceId, Pageable pageable);

    /**
     * 根据节点ID查询执行记录
     *
     * @param nodeId 节点ID
     * @return 执行记录列表
     */
    List<FlowExecutionLog> findByNodeId(String nodeId);

    /**
     * 根据节点ID和流程实例ID查询执行记录
     *
     * @param nodeId        节点ID
     * @param flowInstanceId 流程实例ID
     * @return 执行记录列表
     */
    List<FlowExecutionLog> findByNodeIdAndFlowInstanceId(String nodeId, Long flowInstanceId);

    /**
     * 根据执行类型查询执行记录
     *
     * @param executionType 执行类型
     * @param pageable     分页参数
     * @return 执行记录分页结果
     */
    Page<FlowExecutionLog> findByExecutionType(String executionType, Pageable pageable);

    /**
     * 根据执行状态查询执行记录
     *
     * @param status   执行状态
     * @param pageable 分页参数
     * @return 执行记录分页结果
     */
    Page<FlowExecutionLog> findByStatus(String status, Pageable pageable);

    /**
     * 根据执行时间范围查询执行记录
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageable  分页参数
     * @return 执行记录分页结果
     */
    Page<FlowExecutionLog> findByExecutionTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 根据多个条件组合查询执行记录
     *
     * @param flowInstanceId 流程实例ID
     * @param nodeId        节点ID
     * @param executionType 执行类型
     * @param status        执行状态
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param pageable      分页参数
     * @return 执行记录分页结果
     */
    @Query("SELECT log FROM FlowExecutionLog log WHERE " +
            "(:flowInstanceId IS NULL OR log.flowInstanceId = :flowInstanceId) AND " +
            "(:nodeId IS NULL OR log.nodeId = :nodeId) AND " +
            "(:executionType IS NULL OR log.executionType = :executionType) AND " +
            "(:status IS NULL OR log.status = :status) AND " +
            "(:startTime IS NULL OR log.executionTime >= :startTime) AND " +
            "(:endTime IS NULL OR log.executionTime <= :endTime)")
    Page<FlowExecutionLog> findByConditions(
            @Param("flowInstanceId") Long flowInstanceId,
            @Param("nodeId") String nodeId,
            @Param("executionType") String executionType,
            @Param("status") String status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);

    /**
     * 查询指定流程实例最近的执行记录
     *
     * @param flowInstanceId 流程实例ID
     * @param limit         限制数量
     * @return 执行记录列表
     */
    @Query("SELECT log FROM FlowExecutionLog log WHERE log.flowInstanceId = :flowInstanceId " +
            "ORDER BY log.executionTime DESC")
    List<FlowExecutionLog> findLatestByFlowInstanceId(@Param("flowInstanceId") Long flowInstanceId, Pageable pageable);

    /**
     * 统计指定流程实例的执行记录数量
     *
     * @param flowInstanceId 流程实例ID
     * @return 执行记录数量
     */
    long countByFlowInstanceId(Long flowInstanceId);

    /**
     * 统计指定状态的执行记录数量
     *
     * @param status 执行状态
     * @return 执行记录数量
     */
    long countByStatus(String status);
} 