package com.haue.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReportListVo {

    private Long id;
    //被举报文章作者id
    private Long reportedId;
    //被举报人昵称
    private String nickName;
    //举报分类id
    private Integer reportCategory;
    //举报分类名
    private String reportCategoryName;
    //举报描述
    private String reportDescription;
    //举报人
    private Long createBy;
    //创建时间
    private Date createTime;
    //被举报评论内容
    private String content;
}
