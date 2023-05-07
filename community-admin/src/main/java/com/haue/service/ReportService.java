package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.params.AddReportParam;
import com.haue.params.ReportParam;
import com.haue.params.UpdateReportParam;
import com.haue.pojo.entity.Report;
import com.haue.utils.ResponseResult;


/**
 * 被举报文章表(ArticleReport)表服务接口
 *
 * @author makejava
 * @since 2023-04-19 15:13:37
 */
public interface ReportService extends IService<Report> {

    ResponseResult getArticleList(ReportParam param);

    ResponseResult getCommentList(ReportParam param);

    ResponseResult getArticle(Long id);

    ResponseResult updateReport(UpdateReportParam param);

    ResponseResult getComment(Long id);

    ResponseResult getActivityList(ReportParam param);

    ResponseResult getActivity(Long id);

    ResponseResult addReport(AddReportParam param);
}
