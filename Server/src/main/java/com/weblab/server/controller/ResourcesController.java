package com.weblab.server.controller;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.ResourceDTO;
import com.weblab.server.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resources")
public class ResourcesController {
    private final ResourceService resourceService;

    @PostMapping
    public ApiResult addResource(@RequestBody ResourceDTO resourceDTO) {
        return resourceService.addResource(resourceDTO);
    }

    @PutMapping("/{id}")
    public ApiResult updateResource(@PathVariable long id, @RequestBody ResourceDTO resourceDTO) {
        return resourceService.updateResource(resourceDTO, id);
    }

    @DeleteMapping("/{id}")
    public ApiResult deleteResource(@PathVariable long id) {
        return resourceService.deleteResource(id);
    }

    @GetMapping("/{id}")
    public ApiResult getResource(@PathVariable long id) {
        return resourceService.getResourceById(id);
    }
}
