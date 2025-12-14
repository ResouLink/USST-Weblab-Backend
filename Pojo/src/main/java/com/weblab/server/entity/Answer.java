package com.weblab.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer implements Serializable {
    /**
     * 回答问题表主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 回答内容，文本内容
     */
    private String content;

    /**
     * 老师表主键，回答问题的老师
     */
    private long teacherId;
    /**
     * 问题表主键，表示这个回答回答的是什么问题
     */
    private Long questionId;
}
