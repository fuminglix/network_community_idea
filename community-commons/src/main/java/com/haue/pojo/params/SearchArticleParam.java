package com.haue.pojo.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchArticleParam {

//    搜索的关键字
    @NotBlank(message = "关键字不能为空")
    private String search;
}
