package com.weblab.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weblab.server.dao.*;
import com.weblab.server.dto.QuestionDTO;
import com.weblab.server.entity.*;
import com.weblab.server.event.NotificationEvent;
import com.weblab.server.event.NotificationType;
import com.weblab.server.service.NotificationService;
import com.weblab.server.service.QuestionService;
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

import static com.weblab.common.enums.FileRoleEnum.QUESTION;

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
    private final FileDao fileDao;
    private final AnswerDao answerDao;
    private final StudentDao studentDao;
    private final CourseDao courseDao;

    @Override
    @Transactional
    public void addQuestion(QuestionDTO questionDTO) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Question newQuestion = new Question();
        BeanUtils.copyProperties(questionDTO, newQuestion);
        questionDao.save(newQuestion);
        //向附件表中添加关于回答问题的文件
        fileListDao.setFiles(questionDTO.getFiles(),QUESTION.getFileRole(),newQuestion.getId());
        try {
            List<Notification> notificationList = notificationService.addNotification(newQuestion, teacherCourseDao);
            applicationEventPublisher.publishEvent(new NotificationEvent(this, notificationList, NotificationType.QUESTION));
        } catch (Exception e) {
            log.warn("添加通知失败");
        }
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

        if (updated) {
            //删除之前的记录
            fileListDao.deleteAllQuestionFileList(existing.getId());
            //添加新的fileList记录
            fileListDao.setFiles(questionDTO.getFiles(),QUESTION.getFileRole(),existing.getId());
        }


        if (!updated) {
            log.warn("问题更新失败");
            throw new RuntimeException("更新失败");
        }
        log.info("问题更新成功");
    }

    @Override
    public void deleteQuestion(long id) {
        boolean removed = questionDao.removeById(id);
        fileListDao.deleteAllQuestionFileList(id);

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

        //获取question的附件url
        List<Long> fileIds = fileListDao.getFileIds(QUESTION, id);
        List<String> files = fileDao.getFileUrls(fileIds);
        //获取这个question目前对应的answer
        List<Long> answerIdsByQuestionId = answerDao.getAnswerIdsByQuestionId(id);


        //查找学生和课程名称
        String studentName = studentDao.lambdaQuery()
                .eq(Student::getId, question.getStudentId())
                .select(Student::getName)
                .one()
                .getName();
        String courseName = courseDao.lambdaQuery()
                .eq(Course::getId, question.getCourseId())
                .select(Course::getName)
                .one()
                .getName();

        //组装VO
        QuestionVO vo = new QuestionVO();
        BeanUtils.copyProperties(question, vo);
        vo.setFiles(files);
        vo.setAnswerIds(answerIdsByQuestionId);
        vo.setStudentName(studentName);
        vo.setCourseName(courseName);
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

            List<Long> fileIds = fileListDao.getFileIds(QUESTION, question.getId());
            List<String> files = fileDao.getFileUrls(fileIds);  //返回urls

            List<Long> answerIdsByQuestionId = answerDao.getAnswerIdsByQuestionId(question.getId());

            //查找学生和课程名称
            String studentName = studentDao.lambdaQuery()
                    .eq(Student::getId, question.getStudentId())
                    .select(Student::getName)
                    .one()
                    .getName();
            String courseName = courseDao.lambdaQuery()
                    .eq(Course::getId, question.getCourseId())
                    .select(Course::getName)
                    .one()
                    .getName();


            QuestionVO vo = new QuestionVO();
            BeanUtils.copyProperties(question, vo);
            vo.setFiles(files);
            vo.setAnswerIds(answerIdsByQuestionId);
            vo.setStudentName(studentName);
            vo.setCourseName(courseName);
            return vo;
        }).collect(Collectors.toList());

        return voList;
    }

    @Override
    public List<QuestionVO> getQuestionsToBeAnswered(Long teacherId) {
        List<Long> courseList = teacherCourseDao.lambdaQuery()
                .eq(TeacherCourse::getTeacherId, teacherId)
                .list()
                .stream()
                .map(TeacherCourse::getCourseId).toList();

        List<Question> questionsToBeAnswered =
                questionDao.lambdaQuery()
                        .in(Question::getCourseId, courseList)
                        .eq(Question::getIsAnswered, 0)
                        .list();

        List<QuestionVO> voList = questionsToBeAnswered.stream().map(question -> {
            List<Long> fileIds = fileListDao.getFileIds(QUESTION, question.getId());
            List<String> files = fileDao.getFileUrls(fileIds);

            //查找学生和课程名称
            String studentName = studentDao.lambdaQuery()
                    .eq(Student::getId, question.getStudentId())
                    .select(Student::getName)
                    .one()
                    .getName();
            String courseName = courseDao.lambdaQuery()
                    .eq(Course::getId, question.getCourseId())
                    .select(Course::getName)
                    .one()
                    .getName();

            QuestionVO vo = new QuestionVO();
            BeanUtils.copyProperties(question, vo);
            vo.setFiles(files);
            vo.setStudentName(studentName);
            vo.setCourseName(courseName);
            return vo;
        }).toList();

        return voList;
    }

    @Override
    public List<QuestionVO> getQuestionsRaisedByStudentId(Long studentId) {
        List<Question> questionsList = questionDao.lambdaQuery()
                .eq(Question::getStudentId, studentId)
                .list();

        List<QuestionVO> voList = questionsList.stream().map(question -> {
            List<Long> fileIds = fileListDao.getFileIds(QUESTION, question.getId());
            List<String> files = fileDao.getFileUrls(fileIds);

            List<Long> answerIdsByQuestionId = answerDao.getAnswerIdsByQuestionId(question.getId());

            //查找学生和课程名称
            String studentName = studentDao.lambdaQuery()
                    .eq(Student::getId, question.getStudentId())
                    .select(Student::getName)
                    .one()
                    .getName();
            String courseName = courseDao.lambdaQuery()
                    .eq(Course::getId, question.getCourseId())
                    .select(Course::getName)
                    .one()
                    .getName();

            QuestionVO vo = new QuestionVO();
            BeanUtils.copyProperties(question, vo);
            vo.setFiles(files);
            vo.setAnswerIds(answerIdsByQuestionId);
            vo.setStudentName(studentName);
            vo.setCourseName(courseName);
            return vo;
        }).toList();

        return voList;

    }


    @Override
    public List<QuestionVO> getQuestionsByCourseId(Long courseId) {
        List<Question> questionsList = questionDao.lambdaQuery()
                .eq(Question::getCourseId, courseId)
                .list();

        List<QuestionVO> voList = questionsList.stream().map(question -> {
            List<Long> fileIds = fileListDao.getFileIds(QUESTION, question.getId());
            List<String> files = fileDao.getFileUrls(fileIds);

            List<Long> answerIdsByQuestionId = answerDao.getAnswerIdsByQuestionId(question.getId());

            //查找学生和课程名称
            String studentName = studentDao.lambdaQuery()
                    .eq(Student::getId, question.getStudentId())
                    .select(Student::getName)
                    .one()
                    .getName();
            String courseName = courseDao.lambdaQuery()
                    .eq(Course::getId, question.getCourseId())
                    .select(Course::getName)
                    .one()
                    .getName();

            QuestionVO vo = new QuestionVO();
            BeanUtils.copyProperties(question, vo);
            vo.setFiles(files);
            vo.setAnswerIds(answerIdsByQuestionId);
            vo.setStudentName(studentName);
            vo.setCourseName(courseName);
            return vo;
        }).toList();

        return voList;
    }

}
