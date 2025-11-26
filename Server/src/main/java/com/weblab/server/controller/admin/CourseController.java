package com.weblab.server.controller.admin;


import com.weblab.common.core.domain.ApiResult;
import com.weblab.server.service.CourseService;
import com.weblab.server.vo.CourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 课程管理
 */
@RestController
@RequestMapping("/admin/course/")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @RequestMapping("{id}")
    public ApiResult<CourseVO> getById(@PathVariable Long id){
        return ApiResult.success(courseService.getById(id));
    }

}
