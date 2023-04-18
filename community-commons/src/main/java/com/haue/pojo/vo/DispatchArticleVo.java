package com.haue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DispatchArticleVo {

    private Long id;

    //标题
    private String title;
    //文章摘要
    private String summary;
    //缩略图
    private String thumbnail;
}
