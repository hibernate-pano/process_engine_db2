package com.example.process.model.engine.constant;

/**
 * 流程实例状态常量
 */
public enum InstanceStatus {
    /**
     * 已创建
     */
    CREATED("created", "已创建"),

    /**
     * 运行中
     */
    RUNNING("running", "运行中"),

    /**
     * 已暂停
     */
    SUSPENDED("suspended", "已暂停"),

    /**
     * 已完成
     */
    COMPLETED("completed", "已完成"),

    /**
     * 已取消
     */
    CANCELLED("cancelled", "已取消"),

    /**
     * 失败
     */
    FAILED("failed", "失败");

    private final String code;
    private final String description;

    InstanceStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static InstanceStatus fromCode(String code) {
        for (InstanceStatus status : InstanceStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的流程实例状态: " + code);
    }
} 