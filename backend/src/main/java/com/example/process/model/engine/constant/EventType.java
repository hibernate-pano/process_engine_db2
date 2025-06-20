package com.example.process.model.engine.constant;

/**
 * 事件类型常量
 */
public enum EventType {
    /**
     * 设备状态变化
     */
    DEVICE_STATUS_CHANGE("device_status_change", "设备状态变化"),

    /**
     * 设备数据上报
     */
    DEVICE_DATA_REPORT("device_data_report", "设备数据上报"),

    /**
     * 设备告警
     */
    DEVICE_ALARM("device_alarm", "设备告警"),

    /**
     * 设备故障
     */
    DEVICE_FAULT("device_fault", "设备故障"),

    /**
     * 设备上线
     */
    DEVICE_ONLINE("device_online", "设备上线"),

    /**
     * 设备离线
     */
    DEVICE_OFFLINE("device_offline", "设备离线"),

    /**
     * 设备位置变化
     */
    DEVICE_LOCATION_CHANGE("device_location_change", "设备位置变化"),

    /**
     * 系统事件
     */
    SYSTEM_EVENT("system_event", "系统事件"),

    /**
     * 用户操作
     */
    USER_OPERATION("user_operation", "用户操作"),

    /**
     * 定时触发
     */
    TIMER_TRIGGER("timer_trigger", "定时触发"),

    /**
     * 自定义事件
     */
    CUSTOM_EVENT("custom_event", "自定义事件");

    private final String code;
    private final String description;

    EventType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static EventType fromCode(String code) {
        for (EventType type : EventType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的事件类型: " + code);
    }
} 