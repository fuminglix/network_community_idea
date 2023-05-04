package com.haue.controller;

import com.haue.service.AnswerService;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/discover")
public class StudentController {

    @Autowired
    private AnswerService answerService;

    @GetMapping("/answer/answerList")
    public ResponseResult getAnswerList(@NotNull Integer pageNum,@NotNull Integer pageSize){
        return answerService.getAnswerList(pageNum,pageSize);
    }
}
