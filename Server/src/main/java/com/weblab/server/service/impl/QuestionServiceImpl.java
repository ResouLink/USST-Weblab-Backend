package com.weblab.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weblab.common.enums.FileRoleEnum;
import com.weblab.server.dao.*;
import com.weblab.server.dto.QuestionDTO;
import com.weblab.server.entity.Course;
import com.weblab.server.entity.Notification;
import com.weblab.server.entity.Question;
import com.weblab.server.event.NotificationEvent;
import com.weblab.server.event.NotificationListener;
import com.weblab.server.service.NotificationService;
import com.weblab.server.service.QuestionService;
import com.weblab.server.vo.CourseTeacherVO;
import com.weblab.server.vo.QuestionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao questionDao;
    private final FileListDao fileListDao;
    private final TeacherCourseDao teacherCourseDao;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public void addQuestion(QuestionDTO questionDTO) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Question newQuestion = new Question();
        BeanUtils.copyProperties(questionDTO, newQuestion);
        questionDao.save(newQuestion);
        Notification notification = new Notification();
        try {
            notification = notificationService.addNotification(newQuestion, teacherCourseDao);
        } catch (Exception e) {
            log.warn("添加通知失败");
        }
        applicationEventPublisher.publishEvent(new NotificationEvent(this, notification.getId()));
        log.info("问题添加成功");
    }

    @Override
    public void updateQuestion(QuestionDTO questionDTO, long id) {
        Question existing = questionDao.getById(id);
        if (existing == null) {
            log.warn("问题不存在");
            throw new RuntimeException("问题不存在");
        }

        BeanUtils.copyProperties(questionDTO, existing);
        existing.setId(id);

        boolean updated = questionDao.updateById(existing);
        if (!updated) {
            log.warn("问题更新失败");
            throw new RuntimeException("更新失败");
        }
        log.info("问题更新成功");
    }

    @Override
    public void deleteQuestion(long id) {
        boolean removed = questionDao.removeById(id);
        if (!removed) {
            log.warn("问题删除失败");
            throw new RuntimeException("删除失败，问题不存在");
        }
        log.info("问题删除成功");
    }

    @Override
    public QuestionVO getQuestionById(long id) {
        Question question = questionDao.getById(id);
        if (question == null) {
            log.warn("问题不存在, ID: {}", id);
            throw new RuntimeException("问题不存在");
        }

        List<Long> fileIds = fileListDao.getFileIds(FileRoleEnum.QUESTION, id);
        List<String> files = fileIds.stream().map(String::valueOf).collect(Collectors.toList());

        QuestionVO vo = new QuestionVO();
        BeanUtils.copyProperties(question, vo);
        vo.setFiles(files);
        return vo;
    }

    @Override
    public List<QuestionVO> getQuestions(long page, long size, String keyword) {
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

        return voList;
    }
}
