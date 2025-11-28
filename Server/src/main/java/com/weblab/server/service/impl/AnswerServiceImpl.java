package com.weblab.server.service.impl;

import com.weblab.server.dto.CourseDTO;
import com.weblab.server.dto.PageDto;
import com.weblab.server.service.AnswerService;
import com.weblab.server.vo.AnswerVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {
    @Override
    public AnswerVO getById(Long id) {
        return null;
    }

    @Override
    public List<AnswerVO> list(PageDto pageDto) {
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
