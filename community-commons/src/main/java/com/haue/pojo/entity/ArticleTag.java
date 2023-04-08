package com.haue.pojo.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
/**
 * (ArticleTag)表实体类
 *
 * @author makejava
 * @since 2023-04-01 16:16:39
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("nc_article_tag")
public class ArticleTag implements Serializable {
    //文章id    @TableId
    private Long articleId;
    //标签id    @TableId
    private Long tagId;

}
