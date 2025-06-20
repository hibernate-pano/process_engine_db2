package com.example.process.model.engine;

import com.example.process.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 流程执行记录实体类
 * 用于记录流程执行的历史
 */
@Data
@Entity
@Table(name = "flow_execution_log")
@EqualsAndHashCode(callSuper = true)
public class FlowExecutionLog extends BaseEntity {

    /**
     * 流程实例ID
     */
    @Column(name = "flow_instance_id", nullable = false)
    private Long flowInstanceId;

    /**
     * 节点ID
     */
    @Column(name = "node_id")
    private String nodeId;

    /**
     * 节点类型
     */
    @Column(name = "node_type")
    private String nodeType;

    /**
     * 节点名称
     */
    @Column(name = "node_name")
    private String nodeName;

    /**
     * 执行类型（开始节点、结束节点、节点执行、节点跳过、条件判断、设备动作等）
     */
    @Column(name = "execution_type", nullable = false)
    private String executionType;

    /**
     * 执行状态（成功、失败、跳过等）
     */
    @Column(name = "status", nullable = false)
    private String status;

    /**
     * 执行时间
     */
    @Column(name = "execution_time", nullable = false)
    private LocalDateTime executionTime;

    /**
     * 执行耗时（毫秒）
     */
    @Column(name = "duration")
    private Long duration;

    /**
     * 执行输入（JSON格式）
     */
    @Column(name = "input", columnDefinition = "TEXT")
    private String input;

    /**
     * 执行输出（JSON格式）
     */
    @Column(name = "output", columnDefinition = "TEXT")
    private String output;

    /**
     * 错误信息
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    /**
     * 执行者ID（用户ID或系统ID）
     */
    @Column(name = "executor_id")
    private String executorId;

    /**
     * 执行者类型（用户、系统、设备等）
     */
    @Column(name = "executor_type")
    private String executorType;

    /**
     * 备注
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
} 