package com.example.process.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * 分页请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码（从1开始）
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向（asc, desc）
     */
    private String orderDirection;

    /**
     * 转换为Spring Data的分页请求
     *
     * @return Spring Data分页请求
     */
    public org.springframework.data.domain.PageRequest toSpringPageRequest() {
        Sort sort = null;
        
        if (orderBy != null && !orderBy.isEmpty()) {
            sort = Sort.by(Sort.Direction.fromString(orderDirection), orderBy);
        }
        
        return sort != null
                ? org.springframework.data.domain.PageRequest.of(pageNum - 1, pageSize, sort)
                : org.springframework.data.domain.PageRequest.of(pageNum - 1, pageSize);
    }

    /**
     * 创建一个默认的分页请求（第1页，每页10条）
     *
     * @return 分页请求
     */
    public static PageRequest of() {
        return new PageRequest(1, 10, null, null);
    }

    /**
     * 创建一个分页请求
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 分页请求
     */
    public static PageRequest of(int pageNum, int pageSize) {
        return new PageRequest(pageNum, pageSize, null, null);
    }

    /**
     * 创建一个分页请求
     *
     * @param pageNum        页码
     * @param pageSize       每页大小
     * @param orderBy        排序字段
     * @param orderDirection 排序方向
     * @return 分页请求
     */
    public static PageRequest of(int pageNum, int pageSize, String orderBy, String orderDirection) {
        return new PageRequest(pageNum, pageSize, orderBy, orderDirection);
    }

    /**
     * 获取页码
     *
     * @return 页码
     */
    public Integer getPageNum() {
        return pageNum == null || pageNum < 1 ? 1 : pageNum;
    }

    /**
     * 获取每页大小
     *
     * @return 每页大小
     */
    public Integer getPageSize() {
        return pageSize == null || pageSize < 1 ? 10 : pageSize;
    }
} 