package com.weblab.server.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页查询dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {
    /**
     * 查询字段
     */
    private String keyword;
    /**
     * 查询的页数
     */
    private long page;
    /**
     * 一页的记录数
     */
    private long size;
}

