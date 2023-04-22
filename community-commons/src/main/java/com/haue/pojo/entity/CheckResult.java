package com.haue.pojo.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
/**
 * (CheckResult)表实体类
 *
 * @author makejava
 * @since 2023-04-22 16:41:21
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("sys_check_result")
public class CheckResult implements Serializable {
    //审核记录表id
    @TableId
    private Long id;

    //审核内容id
    private Long contentId;
    //审核内容类型（0 文章，1 评论，2 动态）
    private Integer type;
    //审核结果（0 通过，1 疑似，2 不通过）
    private Integer suggest;
    //结果描述
    private String categoryDescription;
    //结果关键字
    private String keyWord;
}
