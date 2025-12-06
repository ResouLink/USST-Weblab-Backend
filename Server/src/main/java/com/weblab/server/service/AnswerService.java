package com.weblab.server.service;

import com.weblab.server.dto.AnswerDTO;
import com.weblab.server.vo.AnswerVO;

import java.util.List;

public interface AnswerService {
    void addAnswer(AnswerDTO answerDTO);
    void updateAnswer(AnswerDTO answerDTO, long id);
    void deleteAnswer(long id);
    AnswerVO getAnswerById(long id);
    List<AnswerVO> getAnswers(long page, long size, String keyword);
}
