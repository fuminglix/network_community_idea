package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.constants.SystemConstants;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.exception.SystemException;
import com.haue.mapper.ReportMapper;
import com.haue.params.AddReportParam;
import com.haue.params.ReportParam;
import com.haue.params.UpdateReportParam;
import com.haue.pojo.entity.*;
import com.haue.service.*;
import com.haue.utils.SecurityUtils;
import com.haue.vo.*;
import com.haue.pojo.vo.PageVo;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private UserReputationRecordService userReputationRecordService;

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
        report.setUpdateBy(SecurityUtils.getUserId());
        updateById(report); //更新举报记录中的处理结果
        Report record = getById(param.getId()); //获取当前正在处理的记录
        Long reportedId = userService.getById(record.getReportedId()).getId(); //查询被举报用户id
        Long reporterId = userService.getById(record.getCreateBy()).getId(); //查询举报用户id

        UserReputationRecord reportedRecord = new UserReputationRecord(); //新建一条被举报用户信誉修改记录
        UserReputationRecord reporterRecord = new UserReputationRecord(); //新建一条举报用户信誉修改记录
        List<Integer> result = null; //临时变量储存用户一周内扣分记录
        List<UserReputationRecord> records = new ArrayList<>(); //储存被举报用户的扣分记录
        List<UserReputationRecord> list = new ArrayList<>(); //存储举报用户的扣分记录

        List<UserReputationRecord> recordList = getUserReputationRecord(reportedId, reporterId); //查询举报用户和被举报用户的扣分记录
        if (recordList.size() > 0){
            records = recordList.stream() //筛选被举报用户的扣分记录
                    .filter(r -> Objects.equals(r.getUserId(), reportedId))
                    .collect(Collectors.toList());
            list = recordList.stream() //筛选举报用户的扣分记录
                    .filter(r -> Objects.equals(r.getUserId(), reporterId))
                    .collect(Collectors.toList());
        }
        if (param.getRepFlag() == SystemConstants.REPORT_SUCCESS_CODE){ //举报成功时
            reportedRecord.setUserId(reportedId); //设置被举报用户信誉修改记录的用户id
            reporterRecord.setUserId(reporterId); //设置举报用户信誉修改记录的用户id
            if (records.size() > 0 && (result = CheckReputationRecord(records)).size() > 0){ //判断用户是否在一周之内扣过分
                userService.update(null,new LambdaUpdateWrapper<User>() //条件成立，将用户的信誉度按照上次扣分数的两倍扣除
                        .eq(User::getId,reportedId)
                        .setSql("`reputation` = `reputation` + "+ (result.get(0) << SystemConstants.DEFAULT_REPUTATION_REDUCE_FACTOR)));
                reportedRecord.setUpdateScore(result.get(0) << SystemConstants.DEFAULT_REPUTATION_REDUCE_FACTOR); //设置用户信誉度修改记录的修改值
            }else { //用户一周之内没有扣分记录
                userService.update(null,new LambdaUpdateWrapper<User>() //将用户的信誉度按默认的扣除值（-2）扣除
                        .eq(User::getId,reportedId)
                        .setSql("`reputation` = `reputation` + "+ SystemConstants.DEFAULT_REPUTATION_REDUCE));
                reportedRecord.setUpdateScore(SystemConstants.DEFAULT_REPUTATION_REDUCE); //设置用户信誉度修改记录的修改值
            }
            reportedRecord.setAfterReputation(userService.getById(reportedId).getReputation()); //设置用户信誉度修改记录的修改后信誉值
            userReputationRecordService.save(reportedRecord); //保存被举报用的该条扣分记录

            userService.update(null,new LambdaUpdateWrapper<User>() //将举报用户的信誉度按默认值（2）增加
                    .eq(User::getId,reporterId)
                    .setSql("`reputation` = `reputation` + "+ SystemConstants.DEFAULT_REPUTATION_ADD));
            reporterRecord.setUpdateScore(SystemConstants.DEFAULT_REPUTATION_ADD); //和上面相同，设置用户信誉度修改记录的各项值并保存
            reporterRecord.setUserId(reporterId);
            reporterRecord.setAfterReputation(userService.getById(reporterId).getReputation());
            userReputationRecordService.save(reporterRecord);
        }
        if (param.getRepFlag() == SystemConstants.REPORT_FAIL_CODE){ //举报失败
            reporterRecord.setUserId(reporterId); //设置用户信誉度修改记录的用户id
            if (list.size() > 0 && (result = CheckReputationRecord(list)).size() > 0){ //判断举报用户一周之内是否有扣分记录
                userService.update(null,new LambdaUpdateWrapper<User>() //条件成立后处理方式和上面相同
                        .eq(User::getId,reporterId)
                        .setSql("`reputation` = `reputation` + "+ (result.get(0)<< SystemConstants.DEFAULT_REPUTATION_REDUCE_FACTOR)));
                reporterRecord.setUpdateScore(result.get(0)<< SystemConstants.DEFAULT_REPUTATION_REDUCE_FACTOR);
            }else { //同上
                userService.update(null,new LambdaUpdateWrapper<User>()
                        .eq(User::getId,reporterId)
                        .setSql("`reputation` = `reputation` + "+ SystemConstants.DEFAULT_REPUTATION_REDUCE));
                reporterRecord.setUpdateScore(SystemConstants.DEFAULT_REPUTATION_REDUCE);
            }
            reporterRecord.setAfterReputation(userService.getById(reporterId).getReputation());
            userReputationRecordService.save(reporterRecord);
        }
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
     * 添加举报记录
     * @param param
     * @return
     */
    @Override
    @Transactional
    public ResponseResult addReport(AddReportParam param) {
        Report report = BeanCopyUtils.copyBean(param, Report.class);
        Long userId = null;
        if (SystemConstants.CHECK_ARTICLE == param.getType()){
            userId = articleService.getById(param.getContentId()).getCreateBy();
        }else if(SystemConstants.CHECK_COMMENT == param.getType()){
            userId = commentService.getById(param.getContentId()).getCreateBy();
        }else if(SystemConstants.CHECK_ACTIVITY == param.getType()){
            userId = activityContentService.getById(param.getContentId()).getCreateBy();
        }
        report.setReportedId(userId);
        boolean b = save(report);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
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

    /**
     * 检查用户是否在一周内被扣除过信誉度
     * @param records 用户的扣分记录
     * @return 按时间倒序返回用户一周内被扣分数
     */
    public List<Integer> CheckReputationRecord(List<UserReputationRecord> records){
        Date date = new Date();
        List<Integer> result = records.stream()
                .filter(r -> (date.getTime() - r.getCreateTime().getTime()) / SystemConstants.ONE_DAY_MILLIS <= SystemConstants.DEFAULT_REPUTATION_TIME_FACTOR)
                .collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparing(UserReputationRecord::getCreateTime).reversed())
                .map(UserReputationRecord::getUpdateScore)
                .collect(Collectors.toList());
        return result;
    }

    /**
     * 获取举报用户和被举报用户的扣分记录
     * @param reportedId
     * @param reporterId
     * @return
     */
    public List<UserReputationRecord> getUserReputationRecord(Long reportedId,Long reporterId){
        LambdaQueryWrapper<UserReputationRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UserReputationRecord::getUserId,reportedId,reporterId) //查询被举报用户的扣分记录
                .lt(UserReputationRecord::getUpdateScore,SystemConstants.DEFAULT_REPUTATION)
                .orderByDesc(UserReputationRecord::getCreateTime);
        return userReputationRecordService.list(wrapper);
    }
}
