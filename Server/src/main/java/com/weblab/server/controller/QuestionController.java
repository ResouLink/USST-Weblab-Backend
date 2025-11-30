package com.weblab.server.controller;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.CourseDTO;
import com.weblab.server.dto.PageDto;
import com.weblab.server.service.QuestionService;
import com.weblab.server.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions/")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("{id}")
    public ApiResult<QuestionVO> getById(@PathVariable Long id) {
        return ApiResult.success(questionService.getById(id));
    }

    @GetMapping
    public ApiResult<List<QuestionVO>> list(@RequestParam String keyword,
                                            @RequestParam long page,
                                            @RequestParam long size) {
        PageDto pageDto = new PageDto(keyword, page, size);
        return ApiResult.success(questionService.list(pageDto));
    }

    @PostMapping
    public ApiResult<Boolean> save(@RequestBody CourseDTO courseDTO) {
        return ApiResult.success(questionService.save(courseDTO));
    }

    @DeleteMapping("{id}")
    public ApiResult<Boolean> delete(@PathVariable Long id) {
        return ApiResult.success(questionService.delete(id));
    }

    @PutMapping("{id}")
    public ApiResult<Boolean> update(@PathVariable Long id, @RequestBody CourseDTO courseDTO) {
        return ApiResult.success(questionService.update(courseDTO));
    }
}
