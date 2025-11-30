package com.weblab.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.weblab.common.enums.FileRoleEnum;
import com.weblab.common.exception.ServiceException;
import com.weblab.server.dao.AnswerDao;
import com.weblab.server.dao.FileListDao;
import com.weblab.server.dto.CourseDTO;
import com.weblab.server.dto.PageDto;
import com.weblab.server.entity.Answer;
import com.weblab.server.service.AnswerService;
import com.weblab.server.vo.AnswerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerDao answerDao;
    @Autowired
    private FileListDao fileListDao;

    @Override
    public AnswerVO getById(Long id) {
        if (BeanUtil.isEmpty(id)){
            throw new ServiceException("ID不能为空");
        }
        Answer answer = answerDao.getById(id);
        if (BeanUtil.isEmpty(answer)){
            throw new ServiceException("answer不存在");
        }
        List<Long> fileIds = fileListDao.getFileIds(FileRoleEnum.ANSWER, id);
        List<String> stringList = fileIds.stream().map(String::valueOf).toList();
        AnswerVO answerVO = new AnswerVO();
        BeanUtil.copyProperties(answer, answerVO);
        answerVO.setFiles(stringList);
        return answerVO;
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
