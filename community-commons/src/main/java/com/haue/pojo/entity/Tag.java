package com.haue.pojo.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.experimental.Accessors;

import java.io.Serializable;
/**
 * (Tag)表实体类
 *
 * @author makejava
 * @since 2023-04-01 16:13:00
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true) //用于指定实体类的set方法的返回值类型为该类本身
@TableName("nc_tag")
public class Tag implements Serializable {
    //标签id
    @TableId
    private Long id;

    //标签内容
    private String tagName;
}
