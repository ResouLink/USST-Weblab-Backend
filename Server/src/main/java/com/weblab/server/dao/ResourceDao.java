package com.weblab.server.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.server.entity.Resource;
import com.weblab.server.mapper.ResourceMapper;
import org.springframework.stereotype.Service;

@Service
public class ResourceDao extends ServiceImpl<ResourceMapper, Resource> {
}
