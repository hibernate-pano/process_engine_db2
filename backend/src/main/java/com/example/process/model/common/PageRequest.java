package com.example.process.model.common;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * 分页请求参数
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码（从0开始）
     */
    private Integer page = 0;

    /**
     * 每页大小
     */
    private Integer size = 10;

    /**
     * 排序字段
     */
    private String sortBy;

    /**
     * 排序方向（ASC/DESC）
     */
    private String direction = "DESC";

    /**
     * 转换为Spring Data的分页请求
     *
     * @return Spring Data分页请求
     */
    public org.springframework.data.domain.PageRequest toSpringPageRequest() {
        Sort sort = null;
        
        if (sortBy != null && !sortBy.isEmpty()) {
            sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        }
        
        return sort != null
                ? org.springframework.data.domain.PageRequest.of(page, size, sort)
                : org.springframework.data.domain.PageRequest.of(page, size);
    }
} 