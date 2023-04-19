package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.mapper.ReportMapper;
import com.haue.params.ArticleReportParam;
import com.haue.params.CommentReportParam;
import com.haue.pojo.entity.Article;
import com.haue.pojo.entity.Comment;
import com.haue.pojo.entity.Report;
import com.haue.pojo.entity.User;
import com.haue.service.*;
import com.haue.vo.ArticleReportListVo;
import com.haue.pojo.vo.PageVo;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import com.haue.vo.CommentReportListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 被举报文章表(ArticleReport)表服务实现类
 *
 * @author fml
 * @since 2023-04-19 15:13:37
 */
@Service("articleReportService")
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ReportCategoryService reportCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    /**
     * 获取被举报文章列表
     * @param param
     * @return
     */
    @Override
    public ResponseResult getArticleList(ArticleReportParam param) {
        LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(param.getArticleId()),Report::getContentId,param.getArticleId())
                .eq(Objects.nonNull(param.getType()),Report::getType,param.getType())
                .groupBy(Report::getContentId);
        if (Objects.nonNull(param.getTitle())){
            List<Long> list = articleService.list(new LambdaQueryWrapper<Article>().like(Article::getTitle, param.getTitle()))
                    .stream()
                    .map(Article::getId)
                    .distinct()
                    .collect(Collectors.toList());
            if (list.size() == 0){
                list.add(0L);
            }
            wrapper.in(!Objects.nonNull(param.getArticleId()),Report::getContentId,list);
        }
        Page<Report> page = new Page<>(param.getPageNum(), param.getPageSize());
        page(page,wrapper);
        List<ArticleReportListVo> articleReportListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleReportListVo.class);
        if (articleReportListVos.size() > 0){
            articleReportListVos
                    .forEach(article -> {
                        article.setTitle(articleService.getById(article.getContentId()).getTitle());
                        article.setReportCategoryName(reportCategoryService.getById(article.getReportCategory()).getCategoryName());
                    });
        }
        return ResponseResult.okResult(new PageVo(articleReportListVos,page.getTotal()));
    }

    /**
     * 获取被举报的评论
     * @param param
     * @return
     */
    @Override
    public ResponseResult getCommentList(CommentReportParam param) {
        LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(param.getType()),Report::getType,param.getType());
        if (StringUtils.hasText(param.getNickName())){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(User::getNickName,param.getNickName());
            List<User> userList = userService.list(queryWrapper);
            if (userList.size() > 0){
                List<Long> collect = userList.stream()
                        .map(User::getId)
                        .distinct()
                        .collect(Collectors.toList());
                if (collect.size() > 0){
                    wrapper.in(Report::getReportedId,collect);
                }else {
                    wrapper.eq(Report::getReportedId,-1L);
                }
            }else {
                wrapper.eq(Report::getReportedId,-1L);
            }
        }
        if (StringUtils.hasText(param.getContent())){
            LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(Comment::getContent,param.getContent());
            List<Comment> commentList = commentService.list(queryWrapper);
            if (commentList.size() > 0){
                List<Long> collect = commentList.stream()
                        .map(Comment::getId)
                        .distinct()
                        .collect(Collectors.toList());
                if (collect.size() > 0){
                    wrapper.in(Report::getContentId,collect);
                }else {
                    wrapper.eq(Report::getContentId,-1L);
                }
            }else {
                wrapper.eq(Report::getContentId,-1L);
            }
        }
        Page<Report> page = new Page<>(param.getPageNum(), param.getPageSize());
        page(page,wrapper);
        List<CommentReportListVo> listVos = BeanCopyUtils.copyBeanList(page.getRecords(), CommentReportListVo.class);
        listVos.forEach(comment -> {
            comment.setReportCategoryName(reportCategoryService.getById(comment.getReportCategory()).getCategoryName());
            comment.setNickName(userService.getById(comment.getReportedId()).getNickName());
            comment.setContent(commentService.getById(getById(comment.getId()).getContentId()).getContent());
        });
        return ResponseResult.okResult(new PageVo(listVos,page.getTotal()));
    }
}
