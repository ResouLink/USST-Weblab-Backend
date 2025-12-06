package com.weblab.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weblab.common.enums.FileRoleEnum;
import com.weblab.server.dao.FileDao;
import com.weblab.server.dao.FileListDao;
import com.weblab.server.dao.ResourceDao;
import com.weblab.server.dto.ResourceDTO;
import com.weblab.server.entity.Resource;
import com.weblab.server.service.OssFileService;
import com.weblab.server.service.ResourceService;
import com.weblab.server.vo.ResourceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("name", keyword);
        }

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
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);

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
}
