package com.example.process.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页结果
 *
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据列表
     */
    private List<T> content;

    /**
     * 总记录数
     */
    private long totalElements;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 当前页码
     */
    private int page;

    /**
     * 每页大小
     */
    private int size;

    /**
     * 是否为首页
     */
    private boolean first;

    /**
     * 是否为末页
     */
    private boolean last;

    /**
     * 是否为空
     */
    private boolean empty;

    public PageResult(List<T> dtoList, long totalElements, Integer pageNum, Integer pageSize) {
        this.content = dtoList;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        this.page = pageNum;
        this.size = pageSize;
        this.first = pageNum == 0;
        this.last = pageNum == totalPages - 1;
        this.empty = dtoList.isEmpty();
    }

    /**
     * 从Spring Data分页结果转换
     *
     * @param page Spring Data分页结果
     * @param <T>  数据类型
     * @param <R>  原始数据类型
     * @return 分页结果
     */
    public static <T, R> PageResult<T> from(Page<R> page, Function<R, T> converter) {
        PageResult<T> result = new PageResult<>();
        result.setContent(page.getContent().stream().map(converter).collect(Collectors.toList()));
        result.setTotalElements(page.getTotalElements());
        result.setTotalPages(page.getTotalPages());
        result.setPage(page.getNumber());
        result.setSize(page.getSize());
        result.setFirst(page.isFirst());
        result.setLast(page.isLast());
        result.setEmpty(page.isEmpty());
        return result;
    }

    /**
     * 从Spring Data分页结果直接转换（不转换数据类型）
     *
     * @param page Spring Data分页结果
     * @param <T>  数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> from(Page<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setContent(page.getContent());
        result.setTotalElements(page.getTotalElements());
        result.setTotalPages(page.getTotalPages());
        result.setPage(page.getNumber());
        result.setSize(page.getSize());
        result.setFirst(page.isFirst());
        result.setLast(page.isLast());
        result.setEmpty(page.isEmpty());
        return result;
    }

    /**
     * 创建空结果
     *
     * @param <T> 数据类型
     * @return 空的分页结果
     */
    public static <T> PageResult<T> empty() {
        PageResult<T> result = new PageResult<>();
        result.setContent(List.of());
        result.setTotalElements(0);
        result.setTotalPages(0);
        result.setPage(0);
        result.setSize(0);
        result.setFirst(true);
        result.setLast(true);
        result.setEmpty(true);
        return result;
    }
} 