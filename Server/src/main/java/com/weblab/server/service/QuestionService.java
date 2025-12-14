package com.weblab.server.service;

import com.weblab.server.dto.QuestionDTO;
import com.weblab.server.vo.QuestionVO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface QuestionService {
    void addQuestion(QuestionDTO questionDTO) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    void updateQuestion(QuestionDTO questionDTO, long id);
    void deleteQuestion(long id);
    QuestionVO getQuestionById(long id);
    List<QuestionVO> getQuestions(long page, long size, String keyword);
    List<QuestionVO> getQuestionsToBeAnswered(Long teacherId);
    List<QuestionVO> getQuestionsRaisedByStudentId(Long studentId);
    List<QuestionVO> getQuestionsByCourseId(Long courseId);
}
