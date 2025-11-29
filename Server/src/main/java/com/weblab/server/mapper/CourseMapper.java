package com.weblab.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weblab.server.dto.PageDto;
import com.weblab.server.entity.Course;
import com.weblab.server.vo.CourseVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    CourseVO getById(Long id);

    List<CourseVO> list(PageDto pageDto);
}
