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
 * (UserReputationRecord)表实体类
 *
 * @author makejava
 * @since 2023-05-07 14:16:25
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("sys_user_reputation_record")
public class UserReputationRecord implements Serializable {
    //用户信誉度修改记录id
    @TableId
    private Long id;
    //用户id
    private Long userId;
    //本次修改的数值（正负）
    private Integer updateScore;
    //修改时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //修改后的值
    private Integer afterReputation;
}
