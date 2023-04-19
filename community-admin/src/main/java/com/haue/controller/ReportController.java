package com.haue.controller;

import com.haue.params.ArticleReportParam;
import com.haue.params.CommentReportParam;
import com.haue.service.ReportService;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/report")
@Validated
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/article/list")
    public ResponseResult getArticleList(ArticleReportParam param){
        return reportService.getArticleList(param);
    }

    @GetMapping("/comment/list")
    public ResponseResult getCommentList(CommentReportParam param){
        return reportService.getCommentList(param);
    }
}
