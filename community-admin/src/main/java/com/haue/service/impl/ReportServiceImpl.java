package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.constants.SystemConstants;
import com.haue.mapper.ReportMapper;
import com.haue.params.ReportParam;
import com.haue.params.UpdateReportParam;
import com.haue.pojo.entity.*;
import com.haue.service.*;
import com.haue.vo.*;
import com.haue.pojo.vo.PageVo;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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

    @Autowired
    private ActivityContentService activityContentService;

    @Autowired
    private ImgService imgService;

    /**
     * 获取被举报文章列表
     * @param param
     * @return
     */
    @Override
    public ResponseResult getArticleList(ReportParam param) {
        LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Report::getRepFlag, SystemConstants.DEFAULT_REPORT_FLAG_ID)
                .eq(Objects.nonNull(param.getArticleId()),Report::getContentId,param.getArticleId())
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
    public ResponseResult getCommentList(ReportParam param) {
        LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Report::getRepFlag,SystemConstants.DEFAULT_REPORT_FLAG_ID)
                .eq(Objects.nonNull(param.getType()),Report::getType,param.getType());
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

    /**
     * 查询被举报文章内容
     * @param id
     * @return
     */
    @Override
    public ResponseResult getArticle(Long id) {
        Report report = getById(id);
        ReportVo reportVo = BeanCopyUtils.copyBean(report, ReportVo.class);
        reportVo.setReportCategoryName(reportCategoryService.getById(reportVo.getReportCategory()).getCategoryName());
        reportVo.setContent(articleService.getById(reportVo.getContentId()).getContent());
        return ResponseResult.okResult(reportVo);
    }

    /**
     * 修改举报记录
     * @param param
     * @return
     */
    @Override
    public ResponseResult updateReport(UpdateReportParam param) {
        Report report = BeanCopyUtils.copyBean(param, Report.class);
        updateById(report);
        return ResponseResult.okResult();
    }

    /**
     * 查询被举报的评论
     * @param id
     * @return
     */
    @Override
    public ResponseResult getComment(Long id) {
        Report report = getById(id);
        ReportVo reportVo = BeanCopyUtils.copyBean(report, ReportVo.class);
        reportVo.setReportCategoryName(reportCategoryService.getById(reportVo.getReportCategory()).getCategoryName());
        reportVo.setContent(commentService.getById(reportVo.getContentId()).getContent());
        return ResponseResult.okResult(reportVo);
    }

    /**
     * 查询动态列表
     * @param param
     * @return
     */
    @Override
    public ResponseResult getActivityList(ReportParam param) {
        LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
        //组合查询条件
        wrapper.eq(Report::getType,param.getType()) //type为2
                .eq(Report::getRepFlag,SystemConstants.DEFAULT_REPORT_FLAG_ID); //未被处理过
        if (StringUtils.hasText(param.getContent())){ //搜索关键字不为空时
            LambdaQueryWrapper<ActivityContent> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(ActivityContent::getContent,param.getContent()) //从ActivityContent表中查询Content或DispatchContent中包含所查关键字的动态
                    .or()
                    .like(ActivityContent::getDispatchContent,param.getContent());
            List<ActivityContent> contents = activityContentService.list(queryWrapper);
            if (contents.size() > 0){ //判断是否查询到满足条件的动态
                List<Long> list = contents.stream()
                        .map(ActivityContent::getId)
                        .distinct()
                        .collect(Collectors.toList());
                wrapper.in(Report::getContentId,list); //将满足条件的备选动态id加入查询条件
            }else {
                wrapper.eq(Report::getContentId,-1L); //没有满足条件的动态id
            }
        }
        if (StringUtils.hasText(param.getNickName())){ //判断搜索的昵称是否为空
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(User::getNickName,param.getNickName());
            List<User> list = userService.list(queryWrapper);
            if (list.size() > 0){
                List<Long> collect = list.stream()
                        .map(User::getId)
                        .distinct()
                        .collect(Collectors.toList());
                wrapper.in(Report::getReportedId,collect); //将搜索到的备用用户id加入查询条件
            }else {
                wrapper.eq(Report::getReportedId,-1L); //没有满足条件的用户id
            }
        }
        Page<Report> page = new Page<>(param.getPageNum(), param.getPageSize());
        page(page,wrapper);
        List<ReportVo> reportVos = BeanCopyUtils.copyBeanList(page.getRecords(), ReportVo.class); //将查询结果封装
        reportVos.forEach(reportVo -> {
            reportVo.setReportCategoryName(reportCategoryService.getById(reportVo.getReportCategory()).getCategoryName());
        });
        return ResponseResult.okResult(new PageVo(reportVos,page.getTotal()));
    }

    /**
     * 查询动态信息
     * @param id
     * @return
     */
    @Override
    public ResponseResult getActivity(Long id) {
        Report report = getById(id);
        ReportVo reportVo = BeanCopyUtils.copyBean(report, ReportVo.class);//将查询结果封装

        ActivityContent content = activityContentService.getById(reportVo.getContentId()); //查询该条动态信息
        reportVo.setContent(content.getContent()); //set非转发动态的信息
        List<Img> imgs = imgService.list(new LambdaQueryWrapper<Img>().eq(Img::getActivityId, content.getId())); //查询该动态的图片
        reportVo.setReportCategoryName(reportCategoryService.getById(reportVo.getReportCategory()).getCategoryName()); //set被举报类型
        String isRef = content.getIsRef();
        if (!SystemConstants.STATUS_NORMAL.equals(isRef)){ //判断是否是转发动态
            reportVo.setContent(content.getDispatchContent()); //set转发动态时添加的content
            if (SystemConstants.IS_REF_ARTICLE.equals(isRef)){ //转发的文章动态
                Article article = articleService.getById(content.getRefId());
                reportVo.setArticle(BeanCopyUtils.copyBean(article, ArticleVo.class)); //将转发的文章信息封装
            }else {
                List<Img> list = imgService.list(new LambdaQueryWrapper<Img>().eq(Img::getActivityId, content.getRefId()));
                reportVo.setActivityContent(BeanCopyUtils.copyBean(activityContentService.getById(content.getRefId()), ActivityVo.class).setContentImg(getImgToString(list))); //将转发的动态信息封装
            }
        }
        reportVo.setContentImg(getImgToString(imgs)); //set非转发动态的图片
        return ResponseResult.okResult(reportVo);
    }

    /**
     * 将img对象的url属性以String集合的形式返回
     * @param imgs
     * @return
     */
    public List<String> getImgToString(List<Img> imgs){
        List<String> urls = new ArrayList<>();
        if (Objects.nonNull(imgs)) {
            urls = imgs.stream()
                    .map(Img::getUrl)
                    .distinct()
                    .collect(Collectors.toList());
        }
        return urls;
    }
}
