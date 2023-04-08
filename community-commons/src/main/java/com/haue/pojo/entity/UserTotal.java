package com.haue.pojo.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
/**
 * 用户数据统计信息表(UserTotal)表实体类
 *
 * @author makejava
 * @since 2023-04-01 19:15:36
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("nc_user_total")
public class UserTotal implements Serializable {
    //用户id
    @TableId
    private Long id;

    //粉丝数
    private Long fansCount;
    //关注数
    private Long regardCount;
    //点赞数
    private Long loveCount;
    //浏览量
    private Long viewCount;
    //文章总数
    private Long articleCount;
}
