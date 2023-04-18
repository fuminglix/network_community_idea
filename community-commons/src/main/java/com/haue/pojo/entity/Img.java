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
 * (Img)表实体类
 *
 * @author makejava
 * @since 2023-04-12 16:49:17
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@TableName("nc_img")
public class Img implements Serializable {
    //图片id
    @TableId
    private Long id;

    //动态id
    private Long activityId;
    //图片地址
    private String url;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
}
