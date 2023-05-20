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
 * (LoveRecord)表实体类
 *
 * @author makejava
 * @since 2023-05-09 17:40:45
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("nc_love_record")
public class LoveRecord implements Serializable {
    //点赞记录id
    @TableId
    private Long id;

    //点赞内容id
    private Long contentId;
    //被点赞用户id
    private Long contentCreateBy;
    //点赞内容类型（0 文章，1内容）
    private Integer type;
    //点赞人
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    //点赞时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    private String delFlag;
}
