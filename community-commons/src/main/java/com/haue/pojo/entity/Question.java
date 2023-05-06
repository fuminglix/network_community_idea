package com.haue.pojo.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
/**
 * (Question)表实体类
 *
 * @author makejava
 * @since 2023-05-04 19:21:34
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("nc_question")
public class Question implements Serializable {
    //问题id
    @TableId
    private Long id;

    //问题描述
    private String title;
    //详细描述
    private String content;
    //回答该问题的数量
    private Long answerCount;
    //问题类型（0 大学，1中学）
    private Integer type;
    //发布用户id
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    //发布时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
}
