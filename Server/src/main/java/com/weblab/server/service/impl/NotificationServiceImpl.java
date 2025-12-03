package com.weblab.server.service.impl;

import com.weblab.server.dao.NotificationDao;
import com.weblab.server.entity.Notification;
import com.weblab.server.entity.Question;
import com.weblab.server.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final NotificationDao notificationDao;


    /**
     * 添加通知
     *
     * @param t   问题或回答
     * @param d   查询数据dao
     * @param <T>
     * @param <D>
     * @return 添加成功与否
     */
    @Override
    public <T, D> Notification addNotification(T t, D d) {
        Class<?> tClass = t.getClass();
        Class<?> dClass = d.getClass();
        try {
            Field contentField = tClass.getDeclaredField("content");
            Notification notification = new Notification();
            // 爆破
            contentField.setAccessible(true);
            String content = (String) contentField.get(t);
            notification.setContent(content);

            if (tClass.getMethod("getStudentId") != null) {
                // t为问题
                Long studentId = (Long) tClass.getMethod("getStudentId").invoke(t);
                notification.setStudentId(studentId);
                Field courseIdField = tClass.getDeclaredField("courseId");
                contentField.setAccessible(true);
                Long courseId = (Long) courseIdField.get(t);
                Long teacherId = (Long) dClass.getMethod("getByCourseId").invoke(d, courseId); // 获取老师id
                notification.setTeacherId(teacherId);
                notification.setStatus(0);
                notificationDao.save(notification);
                return notification;
            } else {
                // t为回答
                Long teacherId = (Long) tClass.getMethod("getTeacherId").invoke(t);
                notification.setTeacherId(teacherId);
                Field questionIdField = tClass.getDeclaredField("questionId");
                contentField.setAccessible(true);
                Long questionId = (Long) questionIdField.get(t);
                Question question = (Question) dClass.getMethod("getById").invoke(d, questionId);
                Long studentId = question.getStudentId(); // 获取学生id
                notification.setStudentId(studentId);
                notification.setStatus(0);
                notificationDao.save(notification);
                return notification;
            }
        } catch (NoSuchFieldException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            log.error("添加通知失败");
            return null;
        }
    }
}
