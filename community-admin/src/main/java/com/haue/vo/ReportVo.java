package com.haue.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportVo {

    private Long id;
    //被举报文章id
    private Long contentId;
    //举报分类id
    private Integer reportCategory;
    //举报分类名
    private String reportCategoryName;
    //举报描述
    private String reportDescription;
    //文章内容 或 动态
    private String content;
    //动态图片
    private List<String> contentImg;
    private ActivityVo activityContent;
    private ArticleVo article;
    private Date createTime;
}
