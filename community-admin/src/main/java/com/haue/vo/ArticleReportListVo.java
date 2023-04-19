package com.haue.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleReportListVo {

    private Long id;

    //被举报文章id
    private Long contentId;
    //文章标题
    private String title;
    //被举报文章作者id
    private Long reportedId;
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

}
