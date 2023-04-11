package com.haue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendArticleVo {

    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 文章摘要
     */
    private String summary;
    /**
     * 文章创建者
     */
    private Long createBy;
    private Date createTime;
}
