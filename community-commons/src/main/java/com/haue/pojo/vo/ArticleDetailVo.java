package com.haue.pojo.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.haue.pojo.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ArticleDetailVo {
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 所属分类名称
     */
    private String categoryName;
    /**
     * 访问量
     */
    private Long viewCount;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 文章标签
     */
    private List<Tag> tags;
    /**
     * 所属分类id
     */
    private Long categoryId;
    /**
     * 是否允许评论 1是，0否
     */
    private String isComment;
    /**
     * 文章创建者
     */
    private Long createBy;
    /**
     * 文章创建时间
     */
    private Date createTime;
}
