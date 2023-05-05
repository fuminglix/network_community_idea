package com.haue.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionParam {

    private Long id;

    //问题描述
    @NotBlank(message = "问题内容不能为空！")
    private String title;
    //详细描述
    private String content;
    //问题类型
    private Integer type;
}
