package com.weblab.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weblab.common.enums.FileRoleEnum;
import com.weblab.common.enums.RoleEnum;
import com.weblab.server.dao.*;
import com.weblab.server.dto.ResourceDTO;
import com.weblab.server.entity.Resource;
import com.weblab.server.security.SecurityUtil;
import com.weblab.server.service.OssFileService;
import com.weblab.server.service.ResourceService;
import com.weblab.server.vo.ResourceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourceDao resourceDao;
    private final FileListDao fileListDao;
    private final OssFileService ossFileService;
    private final FileDao fileDao;
    private final TeacherCourseDao teacherCourseDao;
    private final StudentCourseDao studentCourseDao;

    @Override
    public void addResource(ResourceDTO resourceDTO) {
        Resource newResource = new Resource();
        BeanUtils.copyProperties(resourceDTO, newResource);
        resourceDao.save(newResource);
        fileListDao.addResourceFileList(resourceDTO.getFiles(),newResource.getId());
        log.info("资源添加成功");
    }

    @Override
    public void updateResource(ResourceDTO resourceDTO, long id) {
        Resource existing = resourceDao.getById(id);
        if (existing == null) {
            log.warn("资源不存在");
            throw new RuntimeException("资源不存在");
        }

        BeanUtils.copyProperties(resourceDTO, existing);
        existing.setId(id);

        boolean updated = resourceDao.updateById(existing);
        fileListDao.deleteAllResourceFileList(id);
        fileListDao.addResourceFileList(resourceDTO.getFiles(),id);
        if (!updated) {
            log.warn("资源更新失败");
            throw new RuntimeException("更新失败");
        }
        log.info("资源更新成功");
    }

    @Override
    public void deleteResource(long id) {
        boolean removed = resourceDao.removeById(id);
        List<Long> files = fileListDao.getResourceFileIds(id);
        files.forEach(ossFileService::deleteFile);
        fileListDao.deleteAllResourceFileList(id);
        if (!removed) {
            log.warn("资源删除失败");
            throw new RuntimeException("删除失败，资源不存在");
        }
        log.info("资源删除成功");
    }

    @Override
    public ResourceVO getResourceById(long id) {
        Resource resource = resourceDao.getById(id);
        List<Long> fileIds = fileListDao.getFileIds(FileRoleEnum.RESOURCE, id);
        List<String> fileUrls = fileDao.getFileUrls(fileIds);
        if (resource == null) {
            log.warn("资源不存在, ID: {}", id);
            throw new RuntimeException("资源不存在");
        }
        ResourceVO vo = new ResourceVO();
        BeanUtils.copyProperties(resource, vo);
        vo.setFileUrls(fileUrls);
        return vo;
    }

    @Override
    public List<ResourceVO> getResources(long page, long size, String keyword) {
        // 1. 分页对象
        Page<Resource> pageParam = new Page<>(page, size);

        // 2. 条件构造器
        QueryWrapper<Resource> queryWrapper = buildResourceQueryWrapper(keyword);

        // 3. 分页查询
        Page<Resource> resultPage = resourceDao.page(pageParam, queryWrapper);

        // 4. 组装 VO
        List<ResourceVO> voList = resultPage.getRecords().stream().map(resource -> {
            List<Long> fileIds = fileListDao.getFileIds(FileRoleEnum.RESOURCE, resource.getId());
            List<String> fileUrls = fileDao.getFileUrls(fileIds);

            ResourceVO vo = new ResourceVO();
            BeanUtils.copyProperties(resource, vo);
            vo.setFileUrls(fileUrls);
            return vo;
        }).collect(Collectors.toList());

        return voList;
    }

    @Override
    public List<ResourceVO> getResourcesByCourseId(long courseId) {
        // 1. 条件构造器：根据 courseId 查询
        QueryWrapper<Resource> queryWrapper = buildResourceByCourseQueryWrapper(courseId);

        // 2. 查询所有匹配的资源
        List<Resource> resources = resourceDao.list(queryWrapper);

        // 3. 组装 VO
        List<ResourceVO> voList = resources.stream().map(resource -> {
            List<Long> fileIds = fileListDao.getFileIds(FileRoleEnum.RESOURCE, resource.getId());
            List<String> fileUrls = fileDao.getFileUrls(fileIds);

            ResourceVO vo = new ResourceVO();
            BeanUtils.copyProperties(resource, vo);
            vo.setFileUrls(fileUrls);
            return vo;
        }).collect(Collectors.toList());

        log.info("查询课程[{}]下的资源成功，共{}条", courseId, voList.size());
        return voList;
    }

    /**
     * @param id 学习资源表主键
     */
    @Override
    public void increaseDownloadCnt(long id) {
        resourceDao.update(null,
                new UpdateWrapper<Resource>()
                        .setSql("download_count = download_count + 1")
                        .eq("id", id)
        );
    }


    /**
     * 构建资源查询条件（带权限检查）
     * 规则：
     * - 管理员：可以看所有资源，包括被隐藏的（visibility=3）
     * - visibility = 3：管理员隐藏，非管理员用户都看不到
     * - visibility = 1：对所有人开放
     * - visibility = 2：只对上传者可见
     * - visibility = 0：只对本班级开放（学生在课程中/老师教该课程）
     */
    private QueryWrapper<Resource> buildResourceQueryWrapper(String keyword) {
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        Long roleId = SecurityUtil.getLoginUser().getUser().getRoleId();
        Long userRole = SecurityUtil.getLoginUser().getUser().getUserRole();
        String role = SecurityUtil.getRole();

        // 1. 添加关键词过滤
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("name", keyword);
        }

        // 2. 管理员可以看所有资源
        if (RoleEnum.ADMIN.value().equals(role)) {
            // log.debug("管理员用户查询资源，不进行可见性过滤");
            return queryWrapper;
        }

        // 3. 非管理员：排除管理员隐藏的资源 (visibility != 3)
        queryWrapper.ne("visibility", 3);

        // 4. 构建复杂的权限条件： AND ( 条件1 OR 条件2 OR 条件3 )
        queryWrapper.and(wrapper -> {
            // 条件1：visibility = 1（对所有人开放）
            wrapper.eq("visibility", 1)

                    // OR 条件2：visibility = 2 且是上传者
                    .or(w -> w.eq("visibility", 2)
                            .eq("uploader_id", roleId)
                            .eq("uploader_role", userRole));

            // OR 条件3：visibility = 0 且在同一课程中
            if (RoleEnum.TEACHER.value().equals(role)) {
                // --- 当前用户是老师 ---
                // 获取老师教授的课程ID列表
                List<Long> teacherCourseIds = teacherCourseDao.query()
                        .eq("teacher_id", roleId)
                        .select("course_id")
                        .list()
                        .stream()
                        .map(tc -> tc.getCourseId())
                        .collect(Collectors.toList());

                // 只有当老师有课程时，才拼接这个OR条件，防止 SQL 语法错误 (IN NULL)
                if (!teacherCourseIds.isEmpty()) {
                    wrapper.or(w -> w.eq("visibility", 0)
                            .in("course_id", teacherCourseIds));
                }
            } else if (RoleEnum.STUDENT.value().equals(role)) {
                // --- 当前用户是学生 ---
                // 获取学生选修的课程ID列表
                List<Long> studentCourseIds = studentCourseDao.query()
                        .eq("student_id", roleId)
                        .select("course_id")
                        .list()
                        .stream()
                        .map(sc -> sc.getCourseId())
                        .collect(Collectors.toList());

                if (!studentCourseIds.isEmpty()) {
                    wrapper.or(w -> w.eq("visibility", 0)
                            .in("course_id", studentCourseIds));
                }
            }
        });

        return queryWrapper;
    }

    /**
     * 构建课程资源查询条件（带权限检查）
     */
    private QueryWrapper<Resource> buildResourceByCourseQueryWrapper(long courseId) {
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        Long roleId = SecurityUtil.getLoginUser().getUser().getRoleId();
        Long userRole = SecurityUtil.getLoginUser().getUser().getUserRole();
        String role = SecurityUtil.getRole();
        // 基础条件：指定课程
        queryWrapper.eq("course_id", courseId);

        // 管理员可以看所有资源
        if (RoleEnum.ADMIN.value().equals(role)) {
            return queryWrapper;
        }

        // 非管理员：排除管理员隐藏的资源
        queryWrapper.ne("visibility", 3);

        // 构建权限条件：AND ( 条件1 OR 条件2 OR 条件3 )
        queryWrapper.and(wrapper -> {
            // 条件1：visibility = 1（对所有人开放）
            wrapper.eq("visibility", 1)

                    // OR 条件2：visibility = 2 且是上传者
                    .or(w -> w.eq("visibility", 2)
                            .eq("uploader_id", roleId)
                            .eq("uploader_role", userRole));

            // OR 条件3：visibility = 0 且在同一课程中
            // 注意：因为已经限定了外层的 course_id，这里只需要判断用户是否有该课程权限即可
            if (RoleEnum.TEACHER.value().equals(role)) {
                // 检查老师是否教授该课程
                long count = teacherCourseDao.query()
                        .eq("teacher_id", roleId)
                        .eq("course_id", courseId)
                        .count();

                if (count > 0) {
                    wrapper.or(w -> w.eq("visibility", 0));
                }
            } else if (RoleEnum.STUDENT.value().equals(role)) {
                // 检查学生是否选修该课程
                long count = studentCourseDao.query()
                        .eq("student_id", roleId)
                        .eq("course_id", courseId)
                        .count();

                if (count > 0) {
                    wrapper.or(w -> w.eq("visibility", 0));
                }
            }
        });

        return queryWrapper;
    }

}
