package com.weblab.server.service;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.ResourceDTO;

public interface ResourceService {
    ApiResult addResource(ResourceDTO resourceDTO);
    ApiResult updateResource(ResourceDTO resourceDTO, long id);
    ApiResult deleteResource(long id);
    ApiResult getResourceById(long id);
}
