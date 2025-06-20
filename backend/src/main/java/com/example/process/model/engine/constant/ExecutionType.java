package com.example.process.model.engine.constant;

/**
 * 执行类型常量
 */
public enum ExecutionType {
    /**
     * 流程开始
     */
    FLOW_START("flow_start", "流程开始"),

    /**
     * 流程结束
     */
    FLOW_END("flow_end", "流程结束"),

    /**
     * 流程暂停
     */
    FLOW_SUSPEND("flow_suspend", "流程暂停"),

    /**
     * 流程恢复
     */
    FLOW_RESUME("flow_resume", "流程恢复"),

    /**
     * 流程取消
     */
    FLOW_CANCEL("flow_cancel", "流程取消"),

    /**
     * 节点执行
     */
    NODE_EXECUTION("node_execution", "节点执行"),

    /**
     * 节点跳过
     */
    NODE_SKIP("node_skip", "节点跳过"),

    /**
     * 条件判断
     */
    CONDITION_EVALUATION("condition_evaluation", "条件判断"),

    /**
     * 设备动作
     */
    DEVICE_ACTION("device_action", "设备动作"),

    /**
     * 事件触发
     */
    EVENT_TRIGGER("event_trigger", "事件触发"),

    /**
     * 定时器触发
     */
    TIMER_TRIGGER("timer_trigger", "定时器触发"),

    /**
     * 变量更新
     */
    VARIABLE_UPDATE("variable_update", "变量更新"),

    /**
     * 用户交互
     */
    USER_INTERACTION("user_interaction", "用户交互"),

    /**
     * 系统操作
     */
    SYSTEM_OPERATION("system_operation", "系统操作"),

    /**
     * 错误处理
     */
    ERROR_HANDLING("error_handling", "错误处理");

    private final String code;
    private final String description;

    ExecutionType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ExecutionType fromCode(String code) {
        for (ExecutionType type : ExecutionType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的执行类型: " + code);
    }
} 