package com.haue.pojo.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
/**
 * (CommunityUser)表实体类
 *
 * @author makejava
 * @since 2023-04-14 14:09:21
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@TableName("nc_community_user")
public class CommunityUser implements Serializable {
    //用户id    @TableId
    private Long userId;
    //社区id    @TableId
    private Long communityId;

}
