package com.weblab.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.weblab.common.core.domain.FileRole;
import com.weblab.common.exception.ServiceException;
import com.weblab.server.dao.FileListDao;
import com.weblab.server.dao.QuestionDao;
import com.weblab.server.dto.CourseDTO;
import com.weblab.server.dto.PageDto;
import com.weblab.server.entity.Question;
import com.weblab.server.service.QuestionService;
import com.weblab.server.vo.QuestionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private FileListDao fileListDao;

    @Override
    public QuestionVO getById(Long id) {
        if (BeanUtil.isEmpty(id)){
            throw new ServiceException("ID不能为空");
        }
        Question question = questionDao.getById(id);
        if (BeanUtil.isEmpty(question)){
            throw new ServiceException("question不存在");
        }
        List<Long> fileIds = fileListDao.getFileIds(FileRole.QUESTION, id);
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        List<String> stringList = fileIds.stream().map(String::valueOf).toList();
        questionVO.setFiles(stringList);
        return questionVO;
    }

    @Override
    public List<QuestionVO> list(PageDto pageDto) {
        if (BeanUtil.isNotEmpty(pageDto)){
            throw new ServiceException("参数不能为空");
        }
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
