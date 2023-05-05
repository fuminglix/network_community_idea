package com.haue.controller;

import com.haue.pojo.param.AnswerParam;
import com.haue.pojo.param.QuestionParam;
import com.haue.service.AnswerService;
import com.haue.service.QuestionService;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/discover")
@Validated
public class StudentController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/answer/answerList")
    public ResponseResult getAnswerList(@NotNull Integer pageNum,@NotNull Integer pageSize,@NotNull Integer type){
        return answerService.getAnswerList(pageNum,pageSize,type);
    }

    @PostMapping("/question")
    public ResponseResult addQuestion(@RequestBody QuestionParam param){
        return questionService.addQuestion(param);
    }

    @GetMapping("/question/questionList")
    public ResponseResult getQuestionList(@NotNull Integer pageNum,@NotNull Integer pageSize){
        return questionService.getQuestionList(pageNum,pageSize);
    }

    @GetMapping("/question/{id}")
    public ResponseResult getQuestion(@PathVariable("id") @NotNull Long id){
        return questionService.getQuestion(id);
    }

    @PostMapping("/answer")
    public ResponseResult addAnswer(@RequestBody AnswerParam param){
        return answerService.addAnswer(param);
    }

    @GetMapping("/answer/{id}")
    public ResponseResult getAnswer(@PathVariable("id") Long id){
        return answerService.getAnswer(id);
    }
}
