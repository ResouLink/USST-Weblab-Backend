package com.weblab.server.service;

import com.weblab.server.dto.CourseDTO;
import com.weblab.server.dto.PageDto;
import com.weblab.server.vo.CourseVO;

import java.util.List;

public interface CourseService {
    CourseVO getById(Long id);
    Boolean save(CourseDTO courseDTO);
    Boolean delete(Long id);
    Boolean update(CourseDTO courseDTO);
    List<CourseVO> list(PageDto pageDto);
}
