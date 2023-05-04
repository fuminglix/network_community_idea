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
 * (QuestionAnswer)表实体类
 *
 * @author makejava
 * @since 2023-05-04 19:21:47
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("nc_question_answer")
public class QuestionAnswer implements Serializable {
    //问题id    @TableId
    private Long questionId;
    //回答id    @TableId
    private Long answerId;

}
