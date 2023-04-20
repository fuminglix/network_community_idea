package com.haue.pojo.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
/**
 * 社区信息表(Community)表实体类
 *
 * @author makejava
 * @since 2023-04-05 15:12:50
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@TableName("nc_community")
public class Community implements Serializable {
    //社区id
    @TableId
    private Long id;
    //社区名
    private String communityName;
    //社区头像
    private String avatar;
    //社区用户数
    private Long userNumber;
    //社区发布内容数
    private Long contentNumber;
    //创建用户id
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
    //社区活跃度
    private Float activity;
    //社区简介
    private String description;
}
