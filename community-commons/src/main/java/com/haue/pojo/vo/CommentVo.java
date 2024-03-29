package com.haue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommentVo {

    private Long id;
    //文章id
    private Long articleId;
    //根评论id
    private Long rootId;
    //评论内容
    private String content;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    private String toCommentUserName;
//    private String toCommentUserAvatar;
    //回复目标评论id
    private Long toCommentId;
    //评论用户id
    private Long createBy;
    //创建时间
    private Date createTime;
    //用户
    private String nickName;
    //用户头像
    private String avatar;
    //子评论
    private List<CommentVo> children;
    private String summary;
}
