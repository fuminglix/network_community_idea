package com.haue.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListParam {
    private Integer pageNum;
    private Integer pageSize;
    private String title;
    private String summary;
}
