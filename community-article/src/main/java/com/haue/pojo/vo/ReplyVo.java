package com.haue.pojo.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyVo {
    private Long id;

    //评论类型（0代表文章评论，1代表动态评论, 2回答评论）
    private String type;
    //根评论id
    private Long rootId;
    //评论内容
    private String content;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    //回复目标评论id
    private Long toCommentId;
    private Long createBy;
    private Date createTime;
    private String myComment;
    private AuthorInfoVo user;
}
