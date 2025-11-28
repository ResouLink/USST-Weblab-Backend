package com.weblab.server.controller.admin;

import com.weblab.common.core.domain.ApiResult;
import com.weblab.server.dto.CourseDTO;
import com.weblab.server.dto.PageDto;
import com.weblab.server.service.AnswerService;
import com.weblab.server.service.CourseService;
import com.weblab.server.vo.AnswerVO;
import com.weblab.server.vo.CourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answer/")
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @GetMapping("{id}")
    public ApiResult<AnswerVO> getById(@PathVariable Long id) {
        return ApiResult.success(answerService.getById(id));
    }

    @GetMapping({"list/{keyword}/{page}/{size}"})
    public ApiResult<List<AnswerVO>> list(@PathVariable String keyword,
                                          @PathVariable long page,
                                          @PathVariable long size) {
        PageDto pageDto = new PageDto(keyword, page, size);
        return ApiResult.success(answerService.list(pageDto));
    }

    @PostMapping
    public ApiResult<Boolean> save(@RequestBody CourseDTO courseDTO) {
        return ApiResult.success(answerService.save(courseDTO));
    }

    @DeleteMapping("{id}")
    public ApiResult<Boolean> delete(@PathVariable Long id) {
        return ApiResult.success(answerService.delete(id));
    }

    @PutMapping("{id}")
    public ApiResult<Boolean> update(@PathVariable Long id, @RequestBody CourseDTO courseDTO) {
        return ApiResult.success(answerService.update(courseDTO));
    }

}
