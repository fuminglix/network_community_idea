package com.haue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyArticleVo {

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
     * 状态（0已发布，1草稿 2 审核中 3 未通过 4 定时发布）
     */
    private String status;
    /**
     * 文章创建者
     */
    private Long createBy;
    private Date createTime;
}
