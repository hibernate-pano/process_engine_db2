package com.example.process.model.engine.constant;

/**
 * 执行状态常量
 */
public enum ExecutionStatus {
    /**
     * 成功
     */
    SUCCESS("success", "成功"),

    /**
     * 失败
     */
    FAILED("failed", "失败"),

    /**
     * 跳过
     */
    SKIPPED("skipped", "跳过"),

    /**
     * 等待中
     */
    WAITING("waiting", "等待中"),

    /**
     * 执行中
     */
    RUNNING("running", "执行中"),

    /**
     * 超时
     */
    TIMEOUT("timeout", "超时"),

    /**
     * 取消
     */
    CANCELLED("cancelled", "取消"),

    /**
     * 未知
     */
    UNKNOWN("unknown", "未知");

    private final String code;
    private final String description;

    ExecutionStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ExecutionStatus fromCode(String code) {
        for (ExecutionStatus status : ExecutionStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的执行状态: " + code);
    }
} 