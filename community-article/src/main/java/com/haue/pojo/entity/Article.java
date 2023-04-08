package com.haue.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 文章表(Article)表实体类
 *
 * @author makejava
 * @since 2023-03-28 15:42:08
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true) //用于指定实体类的set方法的返回值类型为该类本身
@TableName("nc_article")
public class Article implements Serializable {
    @TableId
    private Long id;

    //标题
    private String title;
    //文章摘要
    private String summary;
    //文章内容
    private String content;
    //所属分类id
    private Long categoryId;
    //所属社区id
    private Long communityId;
    /**
     * 临时使用的分类名字段
     */
    @TableField(exist = false)
    private String categoryName;
    @TableField(exist = false)
    private List<Tag> tags;
    //缩略图
    private String thumbnail;
    //是否置顶（0否，1是）
    private String isTop;
    //状态（0已发布，1草稿 2 审核中 3 未通过 4 定时发布）
    private String status;
    //浏览量
    private Long viewCount;
    //收藏数
    private Long collectCount;
    //转发数
    private Long relayCount;
    //点赞数
    private Long loveCount;
    //是否允许评论 1是，0否
    private String isComment;
    //评论数
    private Long commentCount;
    //文章类型（0 原创 1 转载）
    private String articleType;
    //发布形式（0 全部可见 1 仅我可见 2 粉丝可见）
    private String releaseForm;
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //定时发布时间
    private Date scheduled;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
}
