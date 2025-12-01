package com.weblab.server.service.impl;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dao.ResourceDao;
import com.weblab.server.dto.ResourceDTO;
import com.weblab.server.entity.Resource;
import com.weblab.server.service.ResourceService;
import com.weblab.server.vo.ResourceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourceDao resourceDao;

    @Override
    public ApiResult addResource(ResourceDTO resourceDTO) {
        Resource newResource = new Resource();
        BeanUtils.copyProperties(resourceDTO, newResource);
        resourceDao.save(newResource);
        log.info("资源添加成功");
        return ApiResult.success("添加资源成功");
    }

    @Override
    public ApiResult updateResource(ResourceDTO resourceDTO, long id) {
        // 更新有问题
        Resource existing = resourceDao.getById(id);
        if (existing == null) {
            log.warn("资源不存在");
            return ApiResult.fail(1, "资源不存在");
        }

        BeanUtils.copyProperties(resourceDTO, existing);
        existing.setId(id);

        boolean updated = resourceDao.updateById(existing);
        if (updated) {
            log.info("资源更新成功");
            return ApiResult.success("资源更新成功", 1);
        } else {
            log.warn("资源更新失败");
            return ApiResult.fail(1, "更新失败");
        }
    }

    @Override
    public ApiResult deleteResource(long id) {
        // 有问题
        boolean removed = resourceDao.removeById(id);
        if (removed) {
            log.info("资源删除成功");
            return ApiResult.success("资源删除成功");
        } else {
            log.warn("资源删除失败");
            return ApiResult.fail("删除失败，资源不存在");
        }
    }

    @Override
    public ApiResult getResourceById(long id) {
        Resource resource = resourceDao.getById(id);
        if (resource == null) {
            log.warn("资源不存在, ID: {}", id);
            return ApiResult.fail("资源不存在");
        }
        ResourceVO vo = new ResourceVO();
        BeanUtils.copyProperties(resource, vo);
        return ApiResult.success(vo);
    }
}
