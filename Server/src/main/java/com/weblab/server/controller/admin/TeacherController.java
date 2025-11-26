package com.weblab.server.controller.admin;

import com.weblab.common.core.domain.ApiResult;
import com.weblab.server.entity.Teacher;
import com.weblab.server.service.TeacherService;
import com.weblab.server.vo.TeacherVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/teacher/")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("{id}")
    public ApiResult<TeacherVO> getById(@PathVariable Long id){
        return ApiResult.success(teacherService.getById(id));
    }

//    @GetMapping("list/{keyword}/{page}/{size}")
//    public ApiResult<List<TeacherVO>> list(@PathVariable String keyword,
//                                           @PathVariable long page,
//                                           @PathVariable long size){
//        return ApiResult.success(teacherService.list(keyword, page, size));
//    }
}
