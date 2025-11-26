package com.weblab.server.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.server.entity.Course;
import com.weblab.server.mapper.CourseMapper;
import com.weblab.server.vo.CourseVO;
import org.springframework.stereotype.Service;

@Service
public class CourseDao extends ServiceImpl<CourseMapper, Course> {
    public CourseVO getById(Long id) {
        return getBaseMapper().getById(id);
    }
}
