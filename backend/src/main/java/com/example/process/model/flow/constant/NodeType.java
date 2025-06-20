package com.example.process.model.flow.constant;

/**
 * 节点类型常量类
 */
public class NodeType {

    /**
     * 开始节点
     */
    public static final String START = "START";

    /**
     * 结束节点
     */
    public static final String END = "END";

    /**
     * 任务节点
     */
    public static final String TASK = "TASK";

    /**
     * 条件节点
     */
    public static final String CONDITION = "CONDITION";

    /**
     * 设备动作节点
     */
    public static final String DEVICE_ACTION = "DEVICE_ACTION";

    /**
     * 子流程节点
     */
    public static final String SUB_PROCESS = "SUB_PROCESS";

    /**
     * 等待节点
     */
    public static final String WAIT = "WAIT";

    /**
     * 并行网关节点
     */
    public static final String PARALLEL_GATEWAY = "PARALLEL_GATEWAY";

    /**
     * 排他网关节点
     */
    public static final String EXCLUSIVE_GATEWAY = "EXCLUSIVE_GATEWAY";

    /**
     * 事件节点
     */
    public static final String EVENT = "EVENT";

    private NodeType() {
        // 私有构造函数，防止实例化
    }
} 