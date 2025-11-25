package com.weblab.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;




@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourse implements Serializable {
    /**
     * 课程表主键
     */
    private long courseId;
    /**
     * 学生表主键
     */
    private long studentId;
}
