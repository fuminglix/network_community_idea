package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.constants.SystemConstants;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.exception.SystemException;
import com.haue.mapper.CommentMapper;
import com.haue.pojo.entity.Comment;
import com.haue.pojo.entity.User;
import com.haue.pojo.params.GetCommentParam;
import com.haue.pojo.vo.CommentVo;
import com.haue.pojo.vo.PageVo;
import com.haue.service.CommentService;
import com.haue.service.UserService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public ResponseResult commentList(String commentType, GetCommentParam commentParam) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,commentParam.getArticleId())
                .eq(Comment::getRootId,-1)
                .eq(Comment::getType,commentType)
                .orderByAsc(Comment::getCreateTime);

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
    public ResponseResult addComment(Comment comment) {
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        if (comment.getRootId() != -1){
            List<CommentVo> children = getChildren(comment.getRootId());
            return ResponseResult.okResult(children);
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
