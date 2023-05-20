package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.constants.SystemConstants;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.exception.SystemException;
import com.haue.mapper.CommentMapper;
import com.haue.pojo.entity.*;
import com.haue.pojo.params.GetCommentParam;
import com.haue.pojo.params.LoveParam;
import com.haue.pojo.vo.*;
import com.haue.service.*;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import com.haue.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-04-02 16:58:29
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ActivityContentService activityContentService;

    @Autowired
    private LoveRecordService loveRecordService;

//    @Override
//    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
//        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId)
//                .eq(Comment::getRootId,-1)
//                .eq(Comment::getType,commentType)
//                .orderByAsc(Comment::getCreateTime);
//
//        Page<Comment> page = new Page<>(pageNum,pageSize);
//        page(page,queryWrapper);
//        List<CommentVo> commentVoList =  toCommentVoList(page.getRecords());
//
//        //查询子评论，将查询结果封装到commentVoList中的Children属性中
//        commentVoList.stream()
//                .map(commentVo -> commentVo.setChildren(getChildren(commentVo.getId())))
//                .collect(Collectors.toList());
//        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
//    }
    @Override
    public ResponseResult commentList(GetCommentParam commentParam) {
        if (commentParam.getArticleId() < 0){
            Long userId = SecurityUtils.getUserId();
            LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Article::getCreateBy,userId);
            List<Article> list = articleService.list(wrapper);
            if (list.size() > 0){
                List<Long> collect = list.stream().map(Article::getId)
                        .distinct()
                        .collect(Collectors.toList());
                LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(Comment::getArticleId,collect)
                        .eq(Comment::getType,SystemConstants.ARTICLE_STATUS_NORMAL)
                        .orderByDesc(Comment::getCreateTime);
//                List<Comment> comments = list(queryWrapper);
                Page<Comment> page = new Page<>(commentParam.getPageNum(), commentParam.getPageSize());
                page(page,queryWrapper);
                List<CommentVo> commentVoList =  toCommentVoList(page.getRecords());

                //查询子评论，将查询结果封装到commentVoList中的Children属性中
                commentVoList.forEach(commentVo -> {
                    commentVo.setSummary(articleService.getById(commentVo.getArticleId()).getSummary());
                });
                return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
            }
        }
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,commentParam.getArticleId())
                .eq(Comment::getRootId,-1)
                .eq(Comment::getType,commentParam.getType())
                .orderByDesc(Comment::getCreateTime);

        Page<Comment> page = new Page<>(commentParam.getPageNum(),commentParam.getPageSize());
        page(page,queryWrapper);
        List<CommentVo> commentVoList =  toCommentVoList(page.getRecords());

        //查询子评论，将查询结果封装到commentVoList中的Children属性中
        commentVoList.stream()
                .map(commentVo -> commentVo.setChildren(getChildren(commentVo.getId())))
                .collect(Collectors.toList());
        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    @Override
    @Transactional
    public ResponseResult addComment(Comment comment) {
        Integer reputation = userService.getById(SecurityUtils.getUserId()).getReputation();
        if (reputation <= SystemConstants.DEFAULT_REPUTATION){
            return ResponseResult.errorResult(AppHttpCodeEnum.REPUTATION_LOW);
        }
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        List<CommentVo> children = null;
        if (comment.getRootId() != -1){
            children = getChildren(comment.getRootId());
//            return ResponseResult.okResult(children);
        }
        if (SystemConstants.CHECK_ARTICLE == Integer.parseInt(comment.getType())){
            articleService.update(null,new LambdaUpdateWrapper<Article>()
                    .eq(Article::getId,comment.getArticleId())
                    .setSql("`comment_count` = `comment_count` + 1"));
        }else if(SystemConstants.CHECK_ACTIVITY == Integer.parseInt(comment.getType())){
            activityContentService.update(null,new LambdaUpdateWrapper<ActivityContent>()
                    .eq(ActivityContent::getId,comment.getArticleId())
                    .setSql("`comment_count` = `comment_count` + 1"));
        }
        return ResponseResult.okResult(children);
    }

    /**
     * 获取回复当前用户的所有评论
     * @return
     */
    @Override
    public ResponseResult getReply() {
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getToCommentUserId,userId)
                .orderByDesc(Comment::getCreateTime);
        List<Comment> comments = list(wrapper);
        List<ReplyVo> vos = BeanCopyUtils.copyBeanList(comments, ReplyVo.class);
        if (vos.size() > 0){
            vos.forEach(replyVo -> {
                replyVo.setUser(BeanCopyUtils.copyBean(userService.getById(replyVo.getCreateBy()), AuthorInfoVo.class));
                replyVo.setMyComment(getById(replyVo.getToCommentId()).getContent());
            });
        }
        return ResponseResult.okResult(vos);
    }

    /**
     * 获取用户点赞记录
     * @return
     */
    @Override
    public ResponseResult getLove() {
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<LoveRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LoveRecord::getContentCreateBy,userId)
                .orderByDesc(LoveRecord::getCreateTime);
        List<LoveRecord> loveRecords = loveRecordService.list(wrapper);
        List<LoveRecordVo> recordVos = null;
        if (loveRecords.size() > 0){
            recordVos = BeanCopyUtils.copyBeanList(loveRecords, LoveRecordVo.class);
            recordVos.forEach(loveRecord -> {
                loveRecord.setUser(BeanCopyUtils.copyBean(userService.getById(loveRecord.getCreateBy()), AuthorInfoVo.class));
                if (SystemConstants.CHECK_ARTICLE == loveRecord.getType()){
                    loveRecord.setContent(articleService.getById(loveRecord.getContentId()).getSummary());
                } else if (SystemConstants.CHECK_ACTIVITY == loveRecord.getType()) {
                    loveRecord.setContent(activityContentService.getById(loveRecord.getContentId()).getContent());
                }
            });
        }
        return ResponseResult.okResult(recordVos);
    }

    /**
     * 点赞
     * @param param
     * @return
     */
    @Override
    public ResponseResult updateLove(LoveParam param) {
        articleService.update(null,new LambdaUpdateWrapper<Article>()
                .eq(Article::getId,param.getContentId())
                .setSql("`love_count` = `love_count` + 1"));
        LoveRecord loveRecord = BeanCopyUtils.copyBean(param, LoveRecord.class);
        boolean b = loveRecordService.save(loveRecord);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    /**
     * 获取根评论的子评论
     * @param id    根评论的id
     * @return      处理过的toCommentVoList
     */
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id)
                .orderByAsc(Comment::getCreateTime);
        List<Comment> commentVoList = list(queryWrapper);
        return toCommentVoList(commentVoList);
    }

    /**
     * 获取根评论的集合（暂不考虑子评论，子评论单独处理）
     * @param records   List<Comment>
     * @return          处理过的CommentVo集合
     */
    private List<CommentVo> toCommentVoList(List<Comment> records) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(records, CommentVo.class);
        for (CommentVo commentVo : commentVos) {
            //通过creatyBy查询用户的昵称并赋值
            User user = userService.getById(commentVo.getCreateBy());
            commentVo.setNickName(user.getNickName());
            commentVo.setAvatar(user.getAvatar());
            //如果toCommentUserId不为-1才进行查询
            if (commentVo.getToCommentUserId() != -1 && commentVo.getRootId() != commentVo.getToCommentId()){
                //通过toCommentUserId查询用户的昵称并赋值
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }
}
