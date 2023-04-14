package com.haue.pojo.entity;


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
import java.util.Date;

/**
 * 用户关注信息表(RegardFansTotal)表实体类
 *
 * @author makejava
 * @since 2023-04-13 14:40:45
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@TableName("nc_regard_fans_total")
public class RegardFansTotal implements Serializable {
    //用户id    @TableId
    private Long id;
    //被关注用户id    @TableId
    private Long regardId;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
