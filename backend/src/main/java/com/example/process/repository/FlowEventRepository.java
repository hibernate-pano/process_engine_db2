package com.example.process.repository;

import com.example.process.model.engine.FlowEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 事件数据访问接口
 */
@Repository
public interface FlowEventRepository extends JpaRepository<FlowEvent, Long> {

    /**
     * 根据事件类型查询事件
     *
     * @param eventType 事件类型
     * @return 事件列表
     */
    List<FlowEvent> findByEventType(String eventType);

    /**
     * 根据事件类型分页查询事件
     *
     * @param eventType 事件类型
     * @param pageable  分页参数
     * @return 事件分页结果
     */
    Page<FlowEvent> findByEventType(String eventType, Pageable pageable);

    /**
     * 根据事件源ID查询事件
     *
     * @param sourceId 事件源ID
     * @return 事件列表
     */
    List<FlowEvent> findBySourceId(String sourceId);

    /**
     * 根据事件源ID分页查询事件
     *
     * @param sourceId 事件源ID
     * @param pageable 分页参数
     * @return 事件分页结果
     */
    Page<FlowEvent> findBySourceId(String sourceId, Pageable pageable);

    /**
     * 根据事件源类型查询事件
     *
     * @param sourceType 事件源类型
     * @param pageable   分页参数
     * @return 事件分页结果
     */
    Page<FlowEvent> findBySourceType(String sourceType, Pageable pageable);

    /**
     * 根据事件状态查询事件
     *
     * @param status   事件状态
     * @param pageable 分页参数
     * @return 事件分页结果
     */
    Page<FlowEvent> findByStatus(String status, Pageable pageable);

    /**
     * 根据流程实例ID查询事件
     *
     * @param flowInstanceId 流程实例ID
     * @return 事件列表
     */
    List<FlowEvent> findByFlowInstanceId(Long flowInstanceId);

    /**
     * 根据流程实例ID分页查询事件
     *
     * @param flowInstanceId 流程实例ID
     * @param pageable       分页参数
     * @return 事件分页结果
     */
    Page<FlowEvent> findByFlowInstanceId(Long flowInstanceId, Pageable pageable);

    /**
     * 根据节点ID查询事件
     *
     * @param nodeId 节点ID
     * @return 事件列表
     */
    List<FlowEvent> findByNodeId(String nodeId);

    /**
     * 根据事件发生时间范围查询事件
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageable  分页参数
     * @return 事件分页结果
     */
    Page<FlowEvent> findByOccurrenceTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 根据多个条件组合查询事件
     *
     * @param eventType      事件类型
     * @param sourceId       事件源ID
     * @param sourceType     事件源类型
     * @param status         事件状态
     * @param flowInstanceId 流程实例ID
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param pageable       分页参数
     * @return 事件分页结果
     */
    @Query("SELECT e FROM FlowEvent e WHERE " +
            "(:eventType IS NULL OR e.eventType = :eventType) AND " +
            "(:sourceId IS NULL OR e.sourceId = :sourceId) AND " +
            "(:sourceType IS NULL OR e.sourceType = :sourceType) AND " +
            "(:status IS NULL OR e.status = :status) AND " +
            "(:flowInstanceId IS NULL OR e.flowInstanceId = :flowInstanceId) AND " +
            "(:startTime IS NULL OR e.occurrenceTime >= :startTime) AND " +
            "(:endTime IS NULL OR e.occurrenceTime <= :endTime)")
    Page<FlowEvent> findByConditions(
            @Param("eventType") String eventType,
            @Param("sourceId") String sourceId,
            @Param("sourceType") String sourceType,
            @Param("status") String status,
            @Param("flowInstanceId") Long flowInstanceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);

    /**
     * 查询未处理的事件
     *
     * @param status   未处理状态
     * @param pageable 分页参数
     * @return 事件列表
     */
    @Query("SELECT e FROM FlowEvent e WHERE e.status = :status " +
            "ORDER BY e.priority ASC, e.occurrenceTime ASC")
    List<FlowEvent> findUnprocessedEvents(@Param("status") String status, Pageable pageable);

    /**
     * 统计指定类型的事件数量
     *
     * @param eventType 事件类型
     * @return 事件数量
     */
    long countByEventType(String eventType);

    /**
     * 统计指定状态的事件数量
     *
     * @param status 事件状态
     * @return 事件数量
     */
    long countByStatus(String status);

    /**
     * 统计指定时间范围内的事件数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 事件数量
     */
    long countByOccurrenceTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
} 