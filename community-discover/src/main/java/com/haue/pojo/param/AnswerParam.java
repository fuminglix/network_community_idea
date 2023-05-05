package com.haue.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerParam {

    //回答内容
    private String content;
    //被回答问题id
    private Long questionId;
    //被回答问题描述
    private String questionContent;
    //缩略图
    private String thumbnail;
    //状态（0已发布，1草稿 2 审核中 3 未通过）
    private String status;
    //是否允许评论 1是，0否
    private String isComment;
    //文章类型（0 原创 1 转载）
    private String answerType;
    //匿名发布（0 不匿名 1 匿名）
    private String anonymous;

}
