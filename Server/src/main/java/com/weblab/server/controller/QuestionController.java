package com.weblab.server.controller;

import com.weblab.common.enums.RoleEnum;
import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.QuestionDTO;
import com.weblab.server.security.SecurityUtil;
import com.weblab.server.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public ApiResult addQuestion(@RequestBody QuestionDTO questionDTO) {
        try {
            if(RoleEnum.STUDENT.value().equals(SecurityUtil.getRole())) {
                questionDTO.setStudentId(SecurityUtil.getLoginUser().getUser().getRoleId());
            }
            questionService.addQuestion(questionDTO);
            log.info("问题添加成功");
            return ApiResult.success("添加问题成功");
        } catch (Exception e) {
            log.error("问题添加失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResult updateQuestion(@PathVariable long id, @RequestBody QuestionDTO questionDTO) {
        try {
            questionService.updateQuestion(questionDTO, id);
            log.info("问题更新成功");
            return ApiResult.success("问题更新成功", 1);
        } catch (Exception e) {
            log.error("问题更新失败", e);
            return ApiResult.fail(1, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResult deleteQuestion(@PathVariable long id) {
        try {
            questionService.deleteQuestion(id);
            log.info("问题删除成功");
            return ApiResult.success("问题删除成功");
        } catch (Exception e) {
            log.error("问题删除失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResult getQuestion(@PathVariable long id) {
        try {
            return ApiResult.success(questionService.getQuestionById(id));
        } catch (Exception e) {
            log.error("获取问题失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping
    public ApiResult getQuestions(@RequestParam long page, @RequestParam long size, @RequestParam(required = false) String keyword) {
        try {
            return ApiResult.success(questionService.getQuestions(page, size, keyword));
        } catch (Exception e) {
            log.error("获取问题列表失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    //查询老师待回答的问题
    @GetMapping("/teachers/{teacher_id}")
    public ApiResult getQuestionsToBeAnswered(@PathVariable("teacher_id") long teacherId) {
        try {
            if(teacherId == -1) {
                teacherId = SecurityUtil.getLoginUser().getUser().getRoleId();
            }
            return ApiResult.success(questionService.getQuestionsToBeAnswered(teacherId));
        } catch (Exception e) {
            log.info("查询待回答问题失败",e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping("/students/{student_id}")
    public ApiResult getQuestionsOfStutents(@PathVariable("student_id") long studentId) {
        try {
            if(studentId == -1) {
                studentId = SecurityUtil.getLoginUser().getUser().getRoleId();
            }
            return ApiResult.success(questionService.getQuestionsRaisedByStudentId(studentId));
        } catch (Exception e) {
            log.info("学生查询自己提问的问题失败",e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping("/courses/{course_id}")
    public ApiResult getQuestionsByCourseId(@PathVariable("course_id") long courseId) {
        try {
            return ApiResult.success(questionService.getQuestionsByCourseId(courseId));
        } catch (Exception e) {
            log.info("获取一个课程下的所有问题失败",e);
            return ApiResult.fail(e.getMessage());
        }
    }

}
