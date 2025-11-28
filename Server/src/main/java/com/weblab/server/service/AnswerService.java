package com.weblab.server.service;

import com.weblab.server.dto.CourseDTO;
import com.weblab.server.dto.PageDto;
import com.weblab.server.vo.AnswerVO;

import java.util.List;

public interface AnswerService {
    AnswerVO getById(Long id);

    List<AnswerVO> list(PageDto pageDto);

    Boolean save(CourseDTO courseDTO);

    Boolean delete(Long id);

    Boolean update(CourseDTO courseDTO);
}
