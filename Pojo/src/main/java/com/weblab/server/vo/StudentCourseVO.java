package com.weblab.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;




@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseVO implements Serializable {
    /**
     * 返回学生选课列表
     */
    private long[] courses;
}
