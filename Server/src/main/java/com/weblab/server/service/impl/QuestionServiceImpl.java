package com.weblab.server.service.impl;

import com.weblab.server.dto.CourseDTO;
import com.weblab.server.dto.PageDto;
import com.weblab.server.service.QuestionService;
import com.weblab.server.vo.QuestionVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Override
    public QuestionVO getById(Long id) {
        return null;
    }

    @Override
    public List<QuestionVO> list(PageDto pageDto) {
        return List.of();
    }

    @Override
    public Boolean save(CourseDTO courseDTO) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public Boolean update(CourseDTO courseDTO) {
        return null;
    }
}
