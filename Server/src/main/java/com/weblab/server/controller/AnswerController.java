package com.weblab.server.controller;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.AnswerDTO;
import com.weblab.server.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answers")
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public ApiResult addAnswer(@RequestBody AnswerDTO answerDTO) {
        return answerService.addAnswer(answerDTO);
    }

    @PutMapping("/{id}")
    public ApiResult updateAnswer(@PathVariable long id, @RequestBody AnswerDTO answerDTO) {
        return answerService.updateAnswer(answerDTO, id);
    }

    @DeleteMapping("/{id}")
    public ApiResult deleteAnswer(@PathVariable long id) {
        return answerService.deleteAnswer(id);
    }

    @GetMapping("/{id}")
    public ApiResult getAnswer(@PathVariable long id) {
        return answerService.getAnswerById(id);
    }

    @GetMapping
    public ApiResult getAnswers(@RequestParam long page, @RequestParam long size, @RequestParam String keyword) {
        return answerService.getAnswers(page, size, keyword);
    }
}
