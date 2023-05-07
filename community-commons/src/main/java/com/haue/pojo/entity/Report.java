package com.haue.pojo.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;
import lombok.Data;


import java.io.Serializable;
/**
 * 被举报文章表(Report)表实体类
 *
 * @author makejava
 * @since 2023-04-19 17:56:53
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("sys_report")
public class Report implements Serializable {
    //id
    @TableId
    private Long id;

    //被举报内容id
    private Long contentId;
    //举报的内容类型（0 文章，1 评论，2 动态）
    private Integer type;
    //被举报用户id
    private Long reportedId;
    //举报人
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    //举报分类id
    private Integer reportCategory;
    //举报描述
    private String reportDescription;
    //举报时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //处理人
    private Long updateBy;
    //处理时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //结果描述
    private String reportResult;
    //处理结果标志（0代表举报失败，1代表举报成功）
    private Integer repFlag;
    //处理结果标志（0代表举报失败，1代表举报成功）
    private Integer delFlag;
}
