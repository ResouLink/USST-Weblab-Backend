package com.weblab.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weblab.common.enums.FileRoleEnum;
import com.weblab.common.result.ApiResult;
import com.weblab.server.dao.AnswerDao;
import com.weblab.server.dao.FileListDao;
import com.weblab.server.dto.AnswerDTO;
import com.weblab.server.entity.Answer;
import com.weblab.server.service.AnswerService;
import com.weblab.server.vo.AnswerVO;
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
public class AnswerServiceImpl implements AnswerService {
    private final AnswerDao answerDao;
    private final FileListDao fileListDao;

    @Override
    public ApiResult addAnswer(AnswerDTO answerDTO) {
        Answer newAnswer = new Answer();
        BeanUtils.copyProperties(answerDTO, newAnswer);
        answerDao.save(newAnswer);
        log.info("答案添加成功");
        return ApiResult.success("添加答案成功");
    }

    @Override
    public ApiResult updateAnswer(AnswerDTO answerDTO, long id) {
        // 检查是否涉及联表更新
        Answer existing = answerDao.getById(id);
        if (existing == null) {
            log.warn("答案不存在");
            return ApiResult.fail(1, "答案不存在");
        }

        BeanUtils.copyProperties(answerDTO, existing);
        existing.setId(id);

        boolean updated = answerDao.updateById(existing);
        if (updated) {
            log.info("答案更新成功");
            return ApiResult.success("答案更新成功", 1);
        } else {
            log.warn("答案更新失败");
            return ApiResult.fail(1, "更新失败");
        }
    }

    @Override
    public ApiResult deleteAnswer(long id) {
        // 删除答案有问题，要判断是否涉及联表删除
        boolean removed = answerDao.removeById(id);
        if (removed) {
            log.info("答案删除成功");
            return ApiResult.success("答案删除成功");
        } else {
            log.warn("答案删除失败");
            return ApiResult.fail("删除失败，答案不存在");
        }
    }

    @Override
    public ApiResult getAnswerById(long id) {
        Answer answer = answerDao.getById(id);
        if (answer == null) {
            log.warn("答案不存在, ID: {}", id);
            return ApiResult.fail("答案不存在");
        }
        
        List<Long> fileIds = fileListDao.getFileIds(FileRoleEnum.ANSWER, id);
        List<String> files = fileIds.stream().map(String::valueOf).collect(Collectors.toList());
        
        AnswerVO vo = new AnswerVO();
        BeanUtils.copyProperties(answer, vo);
        vo.setFiles(files);
        return ApiResult.success(vo);
    }

    @Override
    public ApiResult getAnswers(long page, long size, String keyword) {
        Page<Answer> pageParam = new Page<>(page, size);

        QueryWrapper<Answer> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("content", keyword);
        }

        Page<Answer> resultPage = answerDao.page(pageParam, queryWrapper);

        List<AnswerVO> voList = resultPage.getRecords().stream().map(answer -> {
            List<Long> fileIds = fileListDao.getFileIds(FileRoleEnum.ANSWER, answer.getId());
            List<String> files = fileIds.stream().map(String::valueOf).collect(Collectors.toList());
            
            AnswerVO vo = new AnswerVO();
            BeanUtils.copyProperties(answer, vo);
            vo.setFiles(files);
            return vo;
        }).collect(Collectors.toList());

        return ApiResult.success(voList);
    }
}
