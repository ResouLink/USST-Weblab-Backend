package com.weblab.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weblab.server.entity.Course;
import com.weblab.server.vo.CourseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
    CourseVO getById(Long id);
}
