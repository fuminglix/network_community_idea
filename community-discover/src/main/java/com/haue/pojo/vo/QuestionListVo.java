package com.haue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class QuestionListVo {

    private Long id;
    //问题描述
    private String title;
    //回答该问题的数量
    private Long answerCount;
    //问题类型（0 大学，1高中）
    private Integer type;
    //详细描述
    private String content;
    private Long createBy;
    private AuthorInfoVo user;
}