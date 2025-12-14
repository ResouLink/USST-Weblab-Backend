package com.weblab.server.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.server.entity.Answer;
import com.weblab.server.mapper.AnswerMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerDao extends ServiceImpl<AnswerMapper, Answer> {
    public List<Long> getAnswerIdsByQuestionId(Long questionId) {
        return this.lambdaQuery()
                .eq(Answer::getQuestionId, questionId)
                .list()
                .stream()
                .map(Answer::getId)
                .toList();
    }
}
