package com.weblab.server.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.server.entity.Answer;
import com.weblab.server.mapper.AnswerMapper;
import org.springframework.stereotype.Service;

@Service
public class AnswerDao extends ServiceImpl<AnswerMapper, Answer> {
}
