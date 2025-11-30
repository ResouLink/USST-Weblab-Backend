package com.weblab.server.controller;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.ResourceDTO;
import com.weblab.server.service.ResourceService;
import com.weblab.server.vo.ResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resources/")
public class ResourcesController {
    @Autowired
    private ResourceService resourcesService;
    @GetMapping("{id}")
    public ApiResult<ResourceVO> getById(@PathVariable Long id) {
        return ApiResult.success(resourcesService.getById(id));
    }

    @PostMapping
    public ApiResult<Boolean> save(@RequestBody ResourceDTO resourceDTO) {
        return ApiResult.success(resourcesService.save(resourceDTO));
    }

    @PutMapping("{id}")
    public ApiResult<Boolean> update(@PathVariable Long id, @RequestBody ResourceDTO resourceDTO) {
        return ApiResult.success(resourcesService.update(id, resourceDTO));
    }

    /**
     * 删除资源
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ApiResult<Boolean> delete(@PathVariable Long id) {
        return ApiResult.success(resourcesService.delete(id));
    }
}
