package com.weblab.server.controller.admin;


import com.weblab.common.core.domain.ApiResult;
import com.weblab.server.dto.CourseDTO;
import com.weblab.server.dto.PageDto;
import com.weblab.server.entity.Course;
import com.weblab.server.service.CourseService;
import com.weblab.server.vo.CourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程管理
 */
@RestController
@RequestMapping("/admin/course/")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("{id}")
    public ApiResult<CourseVO> getById(@PathVariable Long id){
        return ApiResult.success(courseService.getById(id));
    }

    @GetMapping({"list/{keyword}/{page}/{size}"})
    public ApiResult<List<CourseVO>> list(@PathVariable String keyword,
                                          @PathVariable long page,
                                          @PathVariable long size){
        PageDto pageDto = new PageDto(keyword, page, size);
        return ApiResult.success(courseService.list(pageDto));
    }

    @PostMapping
    public ApiResult<Boolean> post(@RequestBody CourseDTO courseDTO){
        return ApiResult.success(courseService.insert(courseDTO));
    }

    @DeleteMapping("{id}")
    public ApiResult< Boolean> delete(@PathVariable Long id){
        return ApiResult.success(courseService.delete(id));
    }

    @PutMapping("{id}")
    public ApiResult<Boolean> put(@PathVariable Long id, @RequestBody CourseDTO courseDTO){
        return ApiResult.success(courseService.update(courseDTO));
    }

}
