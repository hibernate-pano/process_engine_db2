package com.example.process.repository;

import com.example.process.model.engine.DeviceAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备动作数据访问接口
 */
@Repository
public interface DeviceActionRepository extends JpaRepository<DeviceAction, Long> {

    /**
     * 根据流程实例ID查询设备动作
     *
     * @param flowInstanceId 流程实例ID
     * @return 设备动作列表
     */
    List<DeviceAction> findByFlowInstanceId(Long flowInstanceId);

    /**
     * 根据流程实例ID分页查询设备动作
     *
     * @param flowInstanceId 流程实例ID
     * @param pageable      分页参数
     * @return 设备动作分页结果
     */
    Page<DeviceAction> findByFlowInstanceId(Long flowInstanceId, Pageable pageable);

    /**
     * 根据节点ID查询设备动作
     *
     * @param nodeId 节点ID
     * @return 设备动作列表
     */
    List<DeviceAction> findByNodeId(String nodeId);

    /**
     * 根据节点ID和流程实例ID查询设备动作
     *
     * @param nodeId        节点ID
     * @param flowInstanceId 流程实例ID
     * @return 设备动作列表
     */
    List<DeviceAction> findByNodeIdAndFlowInstanceId(String nodeId, Long flowInstanceId);

    /**
     * 根据设备ID查询设备动作
     *
     * @param deviceId 设备ID
     * @return 设备动作列表
     */
    List<DeviceAction> findByDeviceId(String deviceId);

    /**
     * 根据设备ID分页查询设备动作
     *
     * @param deviceId 设备ID
     * @param pageable 分页参数
     * @return 设备动作分页结果
     */
    Page<DeviceAction> findByDeviceId(String deviceId, Pageable pageable);

    /**
     * 根据设备类型查询设备动作
     *
     * @param deviceType 设备类型
     * @param pageable   分页参数
     * @return 设备动作分页结果
     */
    Page<DeviceAction> findByDeviceType(String deviceType, Pageable pageable);

    /**
     * 根据动作类型查询设备动作
     *
     * @param actionType 动作类型
     * @param pageable   分页参数
     * @return 设备动作分页结果
     */
    Page<DeviceAction> findByActionType(String actionType, Pageable pageable);

    /**
     * 根据状态查询设备动作
     *
     * @param status   状态
     * @param pageable 分页参数
     * @return 设备动作分页结果
     */
    Page<DeviceAction> findByStatus(String status, Pageable pageable);

    /**
     * 根据计划执行时间范围查询设备动作
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageable  分页参数
     * @return 设备动作分页结果
     */
    Page<DeviceAction> findByScheduledTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 根据多个条件组合查询设备动作
     *
     * @param flowInstanceId 流程实例ID
     * @param nodeId        节点ID
     * @param deviceId      设备ID
     * @param deviceType    设备类型
     * @param actionType    动作类型
     * @param status        状态
     * @param pageable      分页参数
     * @return 设备动作分页结果
     */
    @Query("SELECT da FROM DeviceAction da WHERE " +
            "(:flowInstanceId IS NULL OR da.flowInstanceId = :flowInstanceId) AND " +
            "(:nodeId IS NULL OR da.nodeId = :nodeId) AND " +
            "(:deviceId IS NULL OR da.deviceId = :deviceId) AND " +
            "(:deviceType IS NULL OR da.deviceType = :deviceType) AND " +
            "(:actionType IS NULL OR da.actionType = :actionType) AND " +
            "(:status IS NULL OR da.status = :status)")
    Page<DeviceAction> findByConditions(
            @Param("flowInstanceId") Long flowInstanceId,
            @Param("nodeId") String nodeId,
            @Param("deviceId") String deviceId,
            @Param("deviceType") String deviceType,
            @Param("actionType") String actionType,
            @Param("status") String status,
            Pageable pageable);

    /**
     * 查询指定设备的最近动作
     *
     * @param deviceId 设备ID
     * @param pageable 分页参数
     * @return 设备动作列表
     */
    @Query("SELECT da FROM DeviceAction da WHERE da.deviceId = :deviceId " +
            "ORDER BY da.startTime DESC NULLS LAST, da.scheduledTime DESC NULLS LAST, da.createdAt DESC")
    List<DeviceAction> findLatestByDeviceId(@Param("deviceId") String deviceId, Pageable pageable);

    /**
     * 查询等待执行的设备动作
     *
     * @param status   等待执行状态
     * @param pageable 分页参数
     * @return 设备动作列表
     */
    @Query("SELECT da FROM DeviceAction da WHERE da.status = :status " +
            "ORDER BY da.scheduledTime ASC NULLS LAST, da.priority ASC, da.createdAt ASC")
    List<DeviceAction> findPendingActions(@Param("status") String status, Pageable pageable);

    /**
     * 统计指定状态的设备动作数量
     *
     * @param status 状态
     * @return 设备动作数量
     */
    long countByStatus(String status);

    /**
     * 统计指定设备的动作数量
     *
     * @param deviceId 设备ID
     * @return 设备动作数量
     */
    long countByDeviceId(String deviceId);
} 