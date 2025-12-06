package com.weblab.server.controller;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.AnswerDTO;
import com.weblab.server.service.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answers")
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public ApiResult addAnswer(@RequestBody AnswerDTO answerDTO) {
        try {
            answerService.addAnswer(answerDTO);
            log.info("答案添加成功");
            return ApiResult.success("添加答案成功");
        } catch (Exception e) {
            log.error("答案添加失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResult updateAnswer(@PathVariable long id, @RequestBody AnswerDTO answerDTO) {
        try {
            answerService.updateAnswer(answerDTO, id);
            log.info("答案更新成功");
            return ApiResult.success("答案更新成功", 1);
        } catch (Exception e) {
            log.error("答案更新失败", e);
            return ApiResult.fail(1, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResult deleteAnswer(@PathVariable long id) {
        try {
            answerService.deleteAnswer(id);
            log.info("答案删除成功");
            return ApiResult.success("答案删除成功");
        } catch (Exception e) {
            log.error("答案删除失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResult getAnswer(@PathVariable long id) {
        try {
            return ApiResult.success(answerService.getAnswerById(id));
        } catch (Exception e) {
            log.error("获取答案失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping
    public ApiResult getAnswers(@RequestParam long page, @RequestParam long size, @RequestParam String keyword) {
        try {
            return ApiResult.success(answerService.getAnswers(page, size, keyword));
        } catch (Exception e) {
            log.error("获取答案列表失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }
}
