package com.weblab.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weblab.common.enums.FileRoleEnum;
import com.weblab.common.result.ApiResult;
import com.weblab.server.dao.FileListDao;
import com.weblab.server.dao.QuestionDao;
import com.weblab.server.dto.QuestionDTO;
import com.weblab.server.entity.Question;
import com.weblab.server.service.QuestionService;
import com.weblab.server.vo.QuestionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao questionDao;
    private final FileListDao fileListDao;

    @Override
    public ApiResult addQuestion(QuestionDTO questionDTO) {
        Question newQuestion = new Question();
        BeanUtils.copyProperties(questionDTO, newQuestion);
        questionDao.save(newQuestion);
        log.info("问题添加成功");
        return ApiResult.success("添加问题成功");
    }

    @Override
    public ApiResult updateQuestion(QuestionDTO questionDTO, long id) {
        Question existing = questionDao.getById(id);
        if (existing == null) {
            log.warn("问题不存在");
            return ApiResult.fail(1, "问题不存在");
        }

        BeanUtils.copyProperties(questionDTO, existing);
        existing.setId(id);

        boolean updated = questionDao.updateById(existing);
        if (updated) {
            log.info("问题更新成功");
            return ApiResult.success("问题更新成功", 1);
        } else {
            log.warn("问题更新失败");
            return ApiResult.fail(1, "更新失败");
        }
    }

    @Override
    public ApiResult deleteQuestion(long id) {
        boolean removed = questionDao.removeById(id);
        if (removed) {
            log.info("问题删除成功");
            return ApiResult.success("问题删除成功");
        } else {
            log.warn("问题删除失败");
            return ApiResult.fail("删除失败，问题不存在");
        }
    }

    @Override
    public ApiResult getQuestionById(long id) {
        Question question = questionDao.getById(id);
        if (question == null) {
            log.warn("问题不存在, ID: {}", id);
            return ApiResult.fail("问题不存在");
        }
        
        List<Long> fileIds = fileListDao.getFileIds(FileRoleEnum.QUESTION, id);
        List<String> files = fileIds.stream().map(String::valueOf).collect(Collectors.toList());
        
        QuestionVO vo = new QuestionVO();
        BeanUtils.copyProperties(question, vo);
        vo.setFiles(files);
        return ApiResult.success(vo);
    }

    @Override
    public ApiResult getQuestions(long page, long size, String keyword) {
        Page<Question> pageParam = new Page<>(page, size);

        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("content", keyword);
        }

        Page<Question> resultPage = questionDao.page(pageParam, queryWrapper);

        List<QuestionVO> voList = resultPage.getRecords().stream().map(question -> {
            List<Long> fileIds = fileListDao.getFileIds(FileRoleEnum.QUESTION, question.getId());
            List<String> files = fileIds.stream().map(String::valueOf).collect(Collectors.toList());
            
            QuestionVO vo = new QuestionVO();
            BeanUtils.copyProperties(question, vo);
            vo.setFiles(files);
            return vo;
        }).collect(Collectors.toList());

        return ApiResult.success(voList);
    }
}
