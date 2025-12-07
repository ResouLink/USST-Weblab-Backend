package com.weblab.server.aop;

import com.weblab.common.enums.RoleEnum;
import com.weblab.server.dao.*;
import com.weblab.server.entity.Student;
import com.weblab.server.entity.Teacher;
import com.weblab.server.entity.Users;
import com.weblab.server.security.SecurityUtil;
import com.weblab.server.vo.ResourceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 资源可见性检查 AOP
 * 
 * 可见性规则：
 * 0 - 只对本班级开放（学生看自己班的课程资源，老师看自己教的课程资源）
 * 1 - 对所有人开放（任何登录用户都能看）
 * 2 - 只对上传者可见（只有上传者能看）
 * 3 - 管理员隐藏（所有用户都看不到）
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ResourceVisibilityAspect {

    private final StudentCourseDao studentCourseDao;
    private final TeacherCourseDao teacherCourseDao;
    private final StudentDao studentDao;
    private final TeacherDao teacherDao;
    private final UserDao userDao;

    /**
     * 拦截 ResourceService.getResourceById 方法
     */
    @Around("execution(* com.weblab.server.service.ResourceService.getResourceById(..))")
    public Object aroundGetResourceById(ProceedingJoinPoint joinPoint) throws Throwable {
        // 执行原方法获取资源
        ResourceVO resourceVO = (ResourceVO) joinPoint.proceed();

        if (resourceVO == null) {
            return null;
        }

        // 检查可见性
        if (!isResourceVisible(resourceVO)) {
            log.warn("用户无权访问资源 ID: {}", resourceVO.getId());
            throw new RuntimeException("无权访问该资源");
        }

        return resourceVO;
    }

    /**
     * 拦截 ResourceService.getResources 方法
     */
    @Around("execution(* com.weblab.server.service.ResourceService.getResources(..))")
    public Object aroundGetResources(ProceedingJoinPoint joinPoint) throws Throwable {
        @SuppressWarnings("unchecked")
        List<ResourceVO> resourceVOList = (List<ResourceVO>) joinPoint.proceed();

        if (resourceVOList == null || resourceVOList.isEmpty()) {
            return resourceVOList;
        }

        // 过滤不可见的资源
        List<ResourceVO> filteredList = resourceVOList.stream()
                .filter(this::isResourceVisible)
                .collect(Collectors.toList());

        log.info("资源列表过滤：原始 {} 条，过滤后 {} 条", resourceVOList.size(), filteredList.size());
        return filteredList;
    }

    /**
     * 拦截 ResourceService.getResourcesByCourseId 方法
     */
    @Around("execution(* com.weblab.server.service.ResourceService.getResourcesByCourseId(..))")
    public Object aroundGetResourcesByCourseId(ProceedingJoinPoint joinPoint) throws Throwable {
        @SuppressWarnings("unchecked")
        List<ResourceVO> resourceVOList = (List<ResourceVO>) joinPoint.proceed();

        if (resourceVOList == null || resourceVOList.isEmpty()) {
            return resourceVOList;
        }

        // 过滤不可见的资源
        List<ResourceVO> filteredList = resourceVOList.stream()
                .filter(this::isResourceVisible)
                .collect(Collectors.toList());

        log.info("课程资源列表过滤：原始 {} 条，过滤后 {} 条", resourceVOList.size(), filteredList.size());
        return filteredList;
    }

    /**
     * 检查当前用户是否能看到该资源
     *
     * @param resourceVO 资源对象
     * @return 是否可见
     */
    private boolean isResourceVisible(ResourceVO resourceVO) {
        String role = SecurityUtil.getRole();
        Long currentUserId = SecurityUtil.getUserId();
        //当时管理员的时候直接无视所有
        if(Objects.equals(role, RoleEnum.ADMIN.value())){
            return true;
        }
        // 未登录用户无法访问任何资源
        if (currentUserId == null) {
            log.warn("未登录用户尝试访问资源");
            return false;
        }

        // 可见性为 3 - 管理员隐藏，所有用户都看不到
        if (resourceVO.getVisibility() == 3) {
            log.debug("资源已被管理员隐藏，用户 {} 无法访问", currentUserId);
            return false;
        }

        // 可见性为 1 - 对所有人开放
        if (resourceVO.getVisibility() == 1) {
            log.debug("资源对所有人开放，用户 {} 可以访问", currentUserId);
            return true;
        }

        // 可见性为 2 - 只对上传者可见
        if (resourceVO.getVisibility() == 2) {
            boolean isUploader = isCurrentUserUploader(resourceVO, currentUserId);
            if (!isUploader) {
                log.debug("资源仅对上传者可见，用户 {} 不是上传者", currentUserId);
            }
            return isUploader;
        }

        // 可见性为 0 - 只对本班级开放
        if (resourceVO.getVisibility() == 0) {
            return isSameCourse(resourceVO.getCourseId(), currentUserId);
        }

        return false;
    }

    /**
     * 检查当前用户是否是资源的上传者
     *
     * @param resourceVO 资源对象
     * @param currentUserId 当前用户 ID
     * @return 是否是上传者
     */
    private boolean isCurrentUserUploader(ResourceVO resourceVO, Long currentUserId) {
        Users currentUser = userDao.getById(currentUserId);
        if (currentUser == null) return false;

        // 检查角色匹配
        if ((resourceVO.getUploaderRole() == 0 && currentUser.getUserRole() == 0) ||
                (resourceVO.getUploaderRole() == 1 && currentUser.getUserRole() == 1)) {

            // 对比 roleId 与 uploaderId
            return Objects.equals(currentUser.getRoleId(), resourceVO.getUploaderId());
        }

        return false;
    }


    /**
     * 检查当前用户是否在同一课程中
     *
     * @param courseId 课程 ID
     * @param currentUserId 当前用户 ID
     * @return 是否在同一课程中
     */
    private boolean isSameCourse(long courseId, Long currentUserId) {
        String currentRole = SecurityUtil.getRole();

        if (RoleEnum.STUDENT.value().equals(currentRole)) {
            // 当前用户是学生，检查是否在该课程中
            Long stuId = userDao.getById(currentUserId).getRoleId();
            return isStudentInCourse(stuId, courseId);
        } else if (RoleEnum.TEACHER.value().equals(currentRole)) {
            // 当前用户是老师，检查是否教该课程
            Long teId = userDao.getById(currentUserId).getRoleId();
            return isTeacherTeachCourse(teId, courseId);
        }

        return false;
    }

    /**
     * 检查学生是否选修了该课程
     *
     * @param userId 用户 ID
     * @param courseId 课程 ID
     * @return 是否选修
     */
    private boolean isStudentInCourse(Long userId, long courseId) {
        // 先查询学生表，找到学生的 ID
        Student student = studentDao.query()
                .eq("id", userId)
                .one();

        if (student == null) {
            log.debug("用户 {} 不是学生或学生信息不存在", userId);
            return false;
        }

        // 检查该学生是否选修了该课程
        long count = studentCourseDao.query()
                .eq("student_id", student.getId())
                .eq("course_id", courseId)
                .count();

        boolean result = count > 0;
        log.debug("检查学生 {} 是否选修课程 {}：{}", student.getId(), courseId, result);
        return result;
    }

    /**
     * 检查老师是否教授该课程
     *
     * @param userId 用户 ID
     * @param courseId 课程 ID
     * @return 是否教授
     */
    private boolean isTeacherTeachCourse(Long userId, long courseId) {
        // 先查询老师表，找到老师的 ID
        Teacher teacher = teacherDao.query()
                .eq("id", userId)
                .one();

        if (teacher == null) {
            log.debug("用户 {} 不是老师或老师信息不存在", userId);
            return false;
        }

        // 检查该老师是否教授了该课程
        long count = teacherCourseDao.query()
                .eq("teacher_id", teacher.getId())
                .eq("course_id", courseId)
                .count();

        boolean result = count > 0;
        log.debug("检查老师 {} 是否教授课程 {}：{}", teacher.getId(), courseId, result);
        return result;
    }
}
