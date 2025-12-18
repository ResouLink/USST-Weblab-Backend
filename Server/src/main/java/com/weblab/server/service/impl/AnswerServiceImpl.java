package com.weblab.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weblab.common.enums.FileRoleEnum;
import com.weblab.server.dao.*;
import com.weblab.server.dto.AnswerDTO;
import com.weblab.server.entity.Answer;
import com.weblab.server.entity.Notification;
import com.weblab.server.entity.Question;
import com.weblab.server.entity.Teacher;
import com.weblab.server.event.NotificationEvent;
import com.weblab.server.event.NotificationType;
import com.weblab.server.service.AnswerService;
import com.weblab.server.service.NotificationService;
import com.weblab.server.vo.AnswerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.weblab.common.enums.FileRoleEnum.ANSWER;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerDao answerDao;
    private final FileListDao fileListDao;
    private final FileDao fileDao;
    private final QuestionDao questionDao;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final TeacherDao teacherDao;

    @Override
    public void addAnswer(AnswerDTO answerDTO) {
        Answer newAnswer = new Answer();
        BeanUtils.copyProperties(answerDTO, newAnswer);
        answerDao.save(newAnswer);
        fileListDao.setFiles(answerDTO.getFiles(),ANSWER.getFileRole(),newAnswer.getId());

        try {
            List<Notification> notificationList = notificationService.addNotification(newAnswer);
            applicationEventPublisher.publishEvent(new NotificationEvent(this, notificationList, NotificationType.ANSWER));
        } catch (Exception e) {
            log.warn("添加通知失败");
        }
        //添加回答成功应该将对应的问题的已回答标记打上
        questionDao.update(null, new LambdaUpdateWrapper<Question>()
                .eq(Question::getId, newAnswer.getId())
                .set(Question::getIsAnswered, 1));

        log.info("答案添加成功");
    }

    @Override
    public void updateAnswer(AnswerDTO answerDTO, long id) {
        Answer existing = answerDao.getById(id);
        if (existing == null) {
            log.warn("答案不存在");
            throw new RuntimeException("答案不存在");
        }

        BeanUtils.copyProperties(answerDTO, existing);
        existing.setId(id);
        boolean updated = answerDao.updateById(existing);

        if(updated) {
            fileListDao.deleteAllAnswerFileList(id);
            fileListDao.setFiles(answerDTO.getFiles(),ANSWER.getFileRole(),existing.getId());
        }
        if (!updated) {
            log.warn("答案更新失败");
            throw new RuntimeException("更新失败");
        }
        log.info("答案更新成功");
    }

    @Override
    public void deleteAnswer(long id) {
        boolean removed = answerDao.removeById(id);
        fileListDao.deleteAllAnswerFileList(id);

        if (!removed) {
            log.warn("答案删除失败");
            throw new RuntimeException("删除失败，答案不存在");
        }
        log.info("答案删除成功");
    }

    @Override
    public AnswerVO getAnswerById(long id) {
        Answer answer = answerDao.getById(id);
        if (answer == null) {
            log.warn("答案不存在, ID: {}", id);
            throw new RuntimeException("答案不存在");
        }
        
        List<Long> fileIds = fileListDao.getFileIds(ANSWER, id);
        List<String> files = fileDao.getFileUrls(fileIds);

        //查找老师姓名
        String teacherName = teacherDao.lambdaQuery().eq(Teacher::getId, answer.getTeacherId()).one().getName();

        AnswerVO vo = new AnswerVO();
        BeanUtils.copyProperties(answer, vo);
        vo.setFiles(files);
        vo.setTeacherName(teacherName);
        return vo;
    }

    @Override
    public List<AnswerVO> getAnswers(long page, long size, String keyword) {
        Page<Answer> pageParam = new Page<>(page, size);

        QueryWrapper<Answer> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("content", keyword);
        }

        Page<Answer> resultPage = answerDao.page(pageParam, queryWrapper);

        List<AnswerVO> voList = resultPage.getRecords().stream().map(answer -> {
            List<Long> fileIds = fileListDao.getFileIds(ANSWER, answer.getId());
            List<String> files = fileDao.getFileUrls(fileIds);

            //查找老师姓名
            String teacherName = teacherDao.lambdaQuery().eq(Teacher::getId, answer.getTeacherId()).one().getName();

            AnswerVO vo = new AnswerVO();
            BeanUtils.copyProperties(answer, vo);
            vo.setFiles(files);
            vo.setTeacherName(teacherName);
            return vo;
        }).collect(Collectors.toList());

        return voList;
    }

    @Override
    public List<AnswerVO> getAnswersByTeacherId(long teacherId) {

        // 1️⃣ 直接查老师的回答
        List<Answer> answers = answerDao.lambdaQuery()
                .eq(Answer::getTeacherId, teacherId)
                .list();

        if (answers.isEmpty()) {
            return List.of();
        }

        // 2️⃣ 组装 VO
        return answers.stream().map(answer -> {
            List<Long> fileIds = fileListDao.getFileIds(ANSWER, answer.getId());
            List<String> files = fileDao.getFileUrls(fileIds);

            //查找老师姓名
            String teacherName = teacherDao.lambdaQuery().eq(Teacher::getId, answer.getTeacherId()).one().getName();

            AnswerVO vo = new AnswerVO();
            BeanUtils.copyProperties(answer, vo);
            vo.setFiles(files);
            vo.setTeacherName(teacherName);
            return vo;
        }).toList();
    }
}
