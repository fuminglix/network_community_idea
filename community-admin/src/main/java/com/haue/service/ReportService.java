package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.params.ArticleReportParam;
import com.haue.params.CommentReportParam;
import com.haue.pojo.entity.Report;
import com.haue.utils.ResponseResult;


/**
 * 被举报文章表(ArticleReport)表服务接口
 *
 * @author makejava
 * @since 2023-04-19 15:13:37
 */
public interface ReportService extends IService<Report> {

    ResponseResult getArticleList(ArticleReportParam param);

    ResponseResult getCommentList(CommentReportParam param);
}
