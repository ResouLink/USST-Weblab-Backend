package com.weblab.server.service;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.QuestionDTO;

public interface QuestionService {
    ApiResult addQuestion(QuestionDTO questionDTO);
    ApiResult updateQuestion(QuestionDTO questionDTO, long id);
    ApiResult deleteQuestion(long id);
    ApiResult getQuestionById(long id);
    ApiResult getQuestions(long page, long size, String keyword);
}
