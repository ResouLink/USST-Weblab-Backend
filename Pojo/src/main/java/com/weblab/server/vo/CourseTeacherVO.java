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
public class CourseTeacherVO implements Serializable {

    /**
     * 老师表主键
     */
    private long teacherId;
    /**
     * 老师姓名
     */
    private String teacherName;
}
