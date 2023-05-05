package com.haue.pojo.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
/**
 * (Answer)表实体类
 *
 * @author makejava
 * @since 2023-05-04 19:21:09
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("nc_answer")
public class Answer implements Serializable {
    //回答id
    @TableId
    private Long id;

    //回答内容
    private String content;
    //被回答问题id
    private Long questionId;
    //被回答问题描述
    private String questionTitle;
    //缩略图
    private String thumbnail;
    //状态（0已发布，1草稿 2 审核中 3 未通过）
    private String status;
    //转发数
    private Long relayCount;
    //点赞数
    private Long loveCount;
    //评论数
    private Long commentCount;
    //是否允许评论 1是，0否
    private String isComment;
    //文章类型（0 原创 1 转载）
    private String answerType;
    //匿名发布（0 不匿名 1 匿名）
    private String anonymous;
    //回答人id
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    //回答时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
}
