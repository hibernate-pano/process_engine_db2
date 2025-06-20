package com.example.process.model.flow.constant;

/**
 * 流程状态常量类
 */
public class FlowStatus {

    /**
     * 草稿
     */
    public static final String DRAFT = "DRAFT";

    /**
     * 已发布
     */
    public static final String PUBLISHED = "PUBLISHED";

    /**
     * 已禁用
     */
    public static final String DISABLED = "DISABLED";

    /**
     * 已归档
     */
    public static final String ARCHIVED = "ARCHIVED";

    private FlowStatus() {
        // 私有构造函数，防止实例化
    }
} 