package com.haue.controller;

import com.haue.params.AddReportParam;
import com.haue.params.ReportParam;
import com.haue.params.UpdateReportParam;
import com.haue.service.ReportService;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/admin/report")
@Validated
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping
    public ResponseResult addReport(@RequestBody AddReportParam param){
        return reportService.addReport(param);
    }

    @GetMapping("/article/list")
    public ResponseResult getArticleList(ReportParam param){
        return reportService.getArticleList(param);
    }

    @GetMapping("/comment/list")
    public ResponseResult getCommentList(ReportParam param){
        return reportService.getCommentList(param);
    }

    @GetMapping("/article/{id}")
    public ResponseResult getArticle(@PathVariable("id") @NotNull Long id){
        return reportService.getArticle(id);
    }

    @PutMapping("/handle")
    public ResponseResult updateReport(@RequestBody UpdateReportParam param){
        return reportService.updateReport(param);
    }

    @GetMapping("/comment/{id}")
    public ResponseResult getComment(@PathVariable("id") @NotNull Long id){
        return reportService.getComment(id);
    }

    @GetMapping("/activity/list")
    public ResponseResult getActivityList(ReportParam param){
        return reportService.getActivityList(param);
    }

    @GetMapping("/activity/{id}")
    public ResponseResult getActivity(@PathVariable("id") Long id){
        return reportService.getActivity(id);
    }

}
