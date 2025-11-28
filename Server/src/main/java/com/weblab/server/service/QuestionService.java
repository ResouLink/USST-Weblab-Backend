package com.weblab.server.service;

import com.weblab.server.dto.CourseDTO;
import com.weblab.server.dto.PageDto;
import com.weblab.server.vo.QuestionVO;

import java.util.List;

public interface QuestionService {
    QuestionVO getById(Long id);

    List<QuestionVO> list(PageDto pageDto);

    Boolean save(CourseDTO courseDTO);

    Boolean delete(Long id);

    Boolean update(CourseDTO courseDTO);
}
