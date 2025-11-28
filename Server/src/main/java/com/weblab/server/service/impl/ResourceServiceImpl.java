package com.weblab.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.weblab.common.exception.ServiceException;
import com.weblab.server.dao.FileDao;
import com.weblab.server.dao.ResourceDao;
import com.weblab.server.dto.ResourceDTO;
import com.weblab.server.entity.Resource;
import com.weblab.server.service.ResourceService;
import com.weblab.server.vo.ResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceDao resourceDao;
    @Autowired
    private FileDao fileDao;


    @Override
    @Transactional // todo 资源获取，没有文件路径获得不到
    public ResourceVO getById(Long id) {
        Resource resource = resourceDao.getById(id);
        return null;
    }

    @Override
    public Boolean save(ResourceDTO resourceDTO) {
        if (BeanUtil.isEmpty(resourceDTO)){
            throw new ServiceException("新增资源为空!");
        }
        Resource resource = new Resource();
        BeanUtil.copyProperties(resourceDTO, resource);
        // todo 附件保存
        return resourceDao.save(resource);
    }

    @Override
    public Boolean update(Long id, ResourceDTO resourceDTO) {
        if (BeanUtil.isEmpty(id) || BeanUtil.isEmpty(resourceDTO)){
            throw new ServiceException("修改资源为空!");
        }
        Resource resource = new Resource();
        resource.setId(id);
        BeanUtil.copyProperties(resourceDTO, resource);
        // todo 附件修改
        return resourceDao.updateById(resource);
    }

    @Override
    @Transactional
    public Boolean delete(Long id) {
        if (BeanUtil.isEmpty(id)){
            throw new ServiceException("删除id为空!");
        }
        // todo 附件删除
        return resourceDao.removeById(id);
    }

}
