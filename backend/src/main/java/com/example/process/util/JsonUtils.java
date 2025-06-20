package com.example.process.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 */
@Slf4j
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 注册Java 8时间模块
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param object 对象
     * @return JSON字符串
     */
    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("转换对象到JSON字符串失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将JSON字符串转换为对象
     *
     * @param json  JSON字符串
     * @param clazz 对象类型
     * @param <T>   对象类型
     * @return 对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            log.error("转换JSON字符串到对象失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将JSON字符串转换为对象列表
     *
     * @param json  JSON字符串
     * @param clazz 对象类型
     * @param <T>   对象类型
     * @return 对象列表
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            log.error("转换JSON字符串到对象列表失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将JSON字符串转换为Map
     *
     * @param json JSON字符串
     * @return Map
     */
    public static Map<String, Object> fromJsonToMap(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            log.error("转换JSON字符串到Map失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将JSON字符串转换为JsonNode
     *
     * @param json JSON字符串
     * @return JsonNode
     */
    public static JsonNode fromJsonToJsonNode(String json) {
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (IOException e) {
            log.error("转换JSON字符串到JsonNode失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将对象转换为另一个对象
     *
     * @param object 源对象
     * @param clazz  目标对象类型
     * @param <T>    目标对象类型
     * @return 目标对象
     */
    public static <T> T convert(Object object, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(object, clazz);
    }

    /**
     * 获取ObjectMapper实例
     *
     * @return ObjectMapper实例
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
} 