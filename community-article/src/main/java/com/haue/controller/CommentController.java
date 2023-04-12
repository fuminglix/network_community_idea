package com.haue.controller;

import com.haue.constants.SystemConstants;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.pojo.entity.Comment;
import com.haue.pojo.params.AddCommentParam;
import com.haue.pojo.params.GetCommentParam;
import com.haue.service.CommentService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/article/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

//    @GetMapping("/commentList")
//    public ResponseResult commentList(@Validated Long articleId, BindingResult result, Integer pageNum, Integer pageSize){
//        if(result.hasErrors()){
//            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMS_NOT_NULL);
//        }
//        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
//    }
    @GetMapping("/commentList")
    public ResponseResult commentList(@Validated GetCommentParam commentParam, BindingResult result){
        if(result.hasErrors()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMS_NOT_NULL);
        }
        return commentService.commentList(commentParam);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentParam addCommentParam){
        Comment comment = BeanCopyUtils.copyBean(addCommentParam, Comment.class);
        return commentService.addComment(comment);
    }
}
