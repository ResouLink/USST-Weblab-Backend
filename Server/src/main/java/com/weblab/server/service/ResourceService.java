package com.weblab.server.service;

import com.weblab.server.dto.ResourceDTO;
import com.weblab.server.vo.ResourceVO;

import java.util.List;

public interface ResourceService {
    void addResource(ResourceDTO resourceDTO);
    void updateResource(ResourceDTO resourceDTO, long id);
    void deleteResource(long id);
    ResourceVO getResourceById(long id);
    List<ResourceVO> getResources(long page,long size, String keyword);
    List<ResourceVO> getResourcesByCourseId(long courseId);
}
