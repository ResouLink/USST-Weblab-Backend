package com.weblab.server.controller;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.QuestionDTO;
import com.weblab.server.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public ApiResult addQuestion(@RequestBody QuestionDTO questionDTO) {
        return questionService.addQuestion(questionDTO);
    }

    @PutMapping("/{id}")
    public ApiResult updateQuestion(@PathVariable long id, @RequestBody QuestionDTO questionDTO) {
        return questionService.updateQuestion(questionDTO, id);
    }

    @DeleteMapping("/{id}")
    public ApiResult deleteQuestion(@PathVariable long id) {
        return questionService.deleteQuestion(id);
    }

    @GetMapping("/{id}")
    public ApiResult getQuestion(@PathVariable long id) {
        return questionService.getQuestionById(id);
    }

    @GetMapping
    public ApiResult getQuestions(@RequestParam long page, @RequestParam long size, @RequestParam String keyword) {
        return questionService.getQuestions(page, size, keyword);
    }
}
