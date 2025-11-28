package com.weblab.server.service;

import com.weblab.server.dto.ResourceDTO;
import com.weblab.server.vo.ResourceVO;

public interface ResourceService {
    ResourceVO getById(Long id);

    Boolean save(ResourceDTO resourceDTO);

    Boolean update(Long id, ResourceDTO resourceDTO);

    Boolean delete(Long id);
}
