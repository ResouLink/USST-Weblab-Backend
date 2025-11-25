package com.weblab.server.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.server.entity.Question;
import com.weblab.server.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

@Service
public class QuestionDao extends ServiceImpl<QuestionMapper, Question> {
}
