package com.haue.pojo.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.haue.pojo.vo.AuthorInfoVo;
import com.haue.pojo.vo.RecommendArticleVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.experimental.Accessors;

import java.io.Serializable;
/**
 * (ActivityContent)表实体类
 *
 * @author makejava
 * @since 2023-04-12 14:46:25
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("nc_activity_content")
public class ActivityContent implements Serializable {
    //动态id    @TableId
    private Long id;

    //动态内容
    private String content;
    //转发标志（0代表非转发 1转发文章 2转发动态）
    private String isRef;
    //转发的文章或者动态id
    private Long refId;
    //转发时添加的内容
    private String dispatchContent;
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
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
    //社区id
    private Long communityId;
    //转发数
    private Long relayCount;
    //评论数
    private Long commentCount;
    //点赞数
    private Long loveCount;
}
