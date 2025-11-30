package com.weblab.server.controller;


import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.CourseDTO;
import com.weblab.server.dto.PageDto;
import com.weblab.server.service.CourseService;
import com.weblab.server.vo.CourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程管理; todo 未做测试功能是否正确
 */
@RestController
@RequestMapping("/api/courses/")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("{id}")
    public ApiResult<CourseVO> getById(@PathVariable Long id) {
        return ApiResult.success(courseService.getById(id));
    }

    @GetMapping
    public ApiResult<List<CourseVO>> list(@RequestParam String keyword,
                                          @RequestParam long page,
                                          @RequestParam long size) {
        PageDto pageDto = new PageDto(keyword, page, size);
        return ApiResult.success(courseService.list(pageDto));
    }

    @PostMapping
    public ApiResult<Boolean> save(@RequestBody CourseDTO courseDTO) {
        return ApiResult.success(courseService.save(courseDTO));
    }

    @DeleteMapping("{id}")
    public ApiResult<Boolean> delete(@PathVariable Long id) {
        return ApiResult.success(courseService.delete(id));
    }

    @PutMapping("{id}")
    public ApiResult<Boolean> update(@PathVariable Long id, @RequestBody CourseDTO courseDTO) {
        return ApiResult.success(courseService.update(courseDTO));
    }

}
