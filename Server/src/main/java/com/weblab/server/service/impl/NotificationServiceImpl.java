package com.weblab.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.weblab.common.enums.RoleEnum;
import com.weblab.server.dao.NotificationDao;
import com.weblab.server.dao.TeacherCourseDao;
import com.weblab.server.dao.UserDao;
import com.weblab.server.entity.Notification;
import com.weblab.server.entity.Question;
import com.weblab.server.entity.TeacherCourse;
import com.weblab.server.entity.Users;
import com.weblab.server.event.NotificationDto;
import com.weblab.server.event.NotificationType;
import com.weblab.server.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final NotificationDao notificationDao;
    private final TeacherCourseDao teacherCourseDao;
    private final UserDao userDao;


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
    public <T, D> List<Notification> addNotification(T t, D d) {
        Class<?> tClass = t.getClass();
        Class<?> dClass = d.getClass();
        try {
            Field contentField = tClass.getDeclaredField("content");
            // 爆破
            contentField.setAccessible(true);
            String content = (String) contentField.get(t);

            if (tClass.getMethod("getStudentId") != null) {
                // t为问题
                Long studentId = (Long) tClass.getMethod("getStudentId").invoke(t);

                Field courseIdField = tClass.getDeclaredField("courseId");
                contentField.setAccessible(true);
                Long courseId = (Long) courseIdField.get(t);
                List<Long> teacherId = (List<Long>) dClass.getMethod("getByCourseId").invoke(d, courseId); // 获取对应授课的老师id列表

                List<Notification> list = new ArrayList<>();
                for (Long teacher : teacherId) {
                    Notification notification = new Notification();
                    notification.setContent(content);
                    notification.setStudentId(studentId);
                    notification.setTeacherId(teacher);
                    notification.setStatus(0);
                    list.add(notification);
                }
                notificationDao.saveBatch(list);
                return list;
            } else {
                // t为回答
                Long teacherId = (Long) tClass.getMethod("getTeacherId").invoke(t);
                Field questionIdField = tClass.getDeclaredField("questionId");
                contentField.setAccessible(true);
                Long questionId = (Long) questionIdField.get(t);
                Question question = (Question) dClass.getMethod("getById").invoke(d, questionId);
                Long studentId = question.getStudentId(); // 获取学生id
                Notification notification = new Notification();
                notification.setContent(content);
                notification.setTeacherId(teacherId);
                notification.setStudentId(studentId);
                notification.setStatus(0);
                notificationDao.save(notification);
                return Collections.singletonList(notification);
            }
        } catch (NoSuchFieldException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            log.error("添加通知失败");
            return null;
        }
    }

}
