package com.weblab.server.service;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.AnswerDTO;

public interface AnswerService {
    ApiResult addAnswer(AnswerDTO answerDTO);
    ApiResult updateAnswer(AnswerDTO answerDTO, long id);
    ApiResult deleteAnswer(long id);
    ApiResult getAnswerById(long id);
    ApiResult getAnswers(long page, long size, String keyword);
}
