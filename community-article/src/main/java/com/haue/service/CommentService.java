package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.pojo.entity.Comment;
import com.haue.pojo.params.GetCommentParam;
import com.haue.pojo.params.LoveParam;
import com.haue.utils.ResponseResult;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-04-02 16:58:29
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(GetCommentParam commentParam);

    ResponseResult addComment(Comment comment);

    ResponseResult getReply();

    ResponseResult getLove();

    ResponseResult updateLove(LoveParam param);
}
