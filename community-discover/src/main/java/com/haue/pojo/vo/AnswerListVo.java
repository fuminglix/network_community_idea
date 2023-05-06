package com.haue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AnswerListVo {

    private Long id;
    //回答内容
    private String content;
    //被回答问题id
    private Long questionId;
    //被回答问题描述
    private String questionTitle;
    //缩略图
    private String thumbnail;
    //转发数
    private Long relayCount;
    //点赞数
    private Long loveCount;
    //评论数
    private Long commentCount;
    //匿名发布（0 匿名 1 不匿名）
    private String anonymous;
    //回答人id
    private Long createBy;
    //回答时间
    private Date createTime;
    private AuthorInfoVo user;
}
