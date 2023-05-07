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
public class ArticleListVo {
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 文章摘要
     */
    private String summary;
    /**
     * 文章摘要
     */
    private String content;
    /**
     * 所属分类名称
     */
    private String categoryName;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 访问量
     */
    private Long viewCount;
    /**
     * 收藏数
     */
    private Long collectCount;
    /**
     * 分享数
     */
    private Long relayCount;
    /**
     * 点赞数
     */
    private Long loveCount;
    /**
     * 评论数
     */
    private Long commentCount;
    /**
     * 文章创建者
     */
    private Long createBy;
    private Date createTime;
}
