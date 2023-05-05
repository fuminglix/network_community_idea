package com.haue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class AnswerVo {
    private Long id;
    //回答内容
    private String content;
    //被回答问题id
    private Long questionId;
    //被回答问题描述
    private String questionTitle;
    //被回答问题详细描述
    private String questionContent;
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
