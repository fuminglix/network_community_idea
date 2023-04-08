package com.haue.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
/**
 * (CommunityUser)表实体类
 *
 * @author makejava
 * @since 2023-04-06 14:19:25
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("nc_community_user")
public class CommunityUser implements Serializable {
    //用户id    @TableId
    private Long userId;
    //社区id    @TableId
    private Long communityId;

}
