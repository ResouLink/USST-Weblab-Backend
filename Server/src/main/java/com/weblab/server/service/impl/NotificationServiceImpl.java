package com.weblab.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.weblab.server.dao.NotificationDao;
import com.weblab.server.dao.QuestionDao;
import com.weblab.server.dao.TeacherCourseDao;
import com.weblab.server.dao.UserDao;
import com.weblab.server.entity.Notification;
import com.weblab.server.entity.Question;
import com.weblab.server.entity.Users;
import com.weblab.server.event.SseClient;
import com.weblab.server.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao notificationDao;
    private final TeacherCourseDao teacherCourseDao;
    private final QuestionDao questionDao;
    private final UserDao userDao;


    /**
     * 添加通知
     *
     * @param t 问题或回答
     * @return 添加成功与否
     */
    @Override
    public <T> List<Notification> addNotification(T t) {
        Class<?> tClass = t.getClass();
        try {
            Field contentField = tClass.getDeclaredField("content");
            // 爆破
            contentField.setAccessible(true);
            String content = (String) contentField.get(t);
            boolean isQuestion = hasMethod(tClass, "getStudentId");

            if (isQuestion) {
                // t为问题
                Long studentId = (Long) tClass.getMethod("getStudentId").invoke(t);

                Field courseIdField = tClass.getDeclaredField("courseId");
                courseIdField.setAccessible(true);
                Long courseId = (Long) courseIdField.get(t);
                Field questionIdField = tClass.getDeclaredField("id");
                questionIdField.setAccessible(true);
                Long questionId = (Long) questionIdField.get(t);
                List<Long> teacherIds = teacherCourseDao.getByCourseId(courseId); // 获取对应授课的老师id列表

                List<Notification> list = new ArrayList<>();
                for (Long teacherId : teacherIds) {
                    Notification notification = new Notification();
                    notification.setContent(content);
                    notification.setStudentId(studentId);
                    notification.setTeacherId(teacherId);
                    notification.setStatus(0);
                    notification.setQuestionId(questionId);

                    // 先保存到数据库
                    notificationDao.save(notification);
                    list.add(notification);
                }
                return list;
            } else {
                // t为回答
                Long teacherId = (Long) tClass.getMethod("getTeacherId").invoke(t);
                Field questionIdField = tClass.getDeclaredField("questionId");
                questionIdField.setAccessible(true);
                Long questionId = (Long) questionIdField.get(t);
                Question question = questionDao.getById(questionId);
                Long studentId = question.getStudentId(); // 获取学生id
                Notification notification = new Notification();
                notification.setContent(content);
                notification.setTeacherId(teacherId);
                notification.setStudentId(studentId);
                notification.setQuestionId(questionId);
                notification.setStatus(0);

                // 先保存到数据库
                notificationDao.save(notification);

                return Collections.singletonList(notification);
            }
        } catch (NoSuchFieldException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            log.error("添加通知失败", e);
            return null;
        }
    }

    /**
     * 获取用户未读通知数量
     */
    @Override
    public long getUnreadCount(Long userId) {
        if (userId == null) {
            return 0;
        }
        return notificationDao.count(new LambdaQueryWrapper<Notification>()
                .eq(Notification::getStudentId, userId)
                .eq(Notification::getStatus, 0));
    }

    /**
     * 获取用户的通知列表
     * @param userId 用户ID
     * @param status 状态：null表示所有，0表示未读，1表示已读
     */
    @Override
    public List<Notification> getNotificationsByUserId(Long userId, Integer status) {
        if (userId == null) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<Notification>()
                .eq(Notification::getStudentId, userId)
                .orderByDesc(Notification::getId);

        if (status != null) {
            wrapper.eq(Notification::getStatus, status);
        }

        return notificationDao.list(wrapper);
    }

    /**
     * 标记单条通知为已读
     */
    @Override
    public void markAsRead(Long notificationId) {
        if (notificationId == null) {
            return;
        }
        Notification notification = notificationDao.getById(notificationId);
        if (notification != null && notification.getStatus() == 0) {
            notification.setStatus(1);
            notificationDao.updateById(notification);
            log.info("通知{}已标记为已读", notificationId);
        }
    }

    /**
     * 标记用户的所有通知为已读
     */
    @Override
    public void markAllAsRead(Long userId) {
        if (userId == null) {
            return;
        }
        notificationDao.update(null, new LambdaUpdateWrapper<Notification>()
                .eq(Notification::getStudentId, userId)
                .eq(Notification::getStatus, 0)
                .set(Notification::getStatus, 1));
        log.info("用户{}的所有通知已标记为已读", userId);
    }

    private boolean hasMethod(Class<?> clazz, String methodName) {
        try {
            clazz.getMethod(methodName);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

}
