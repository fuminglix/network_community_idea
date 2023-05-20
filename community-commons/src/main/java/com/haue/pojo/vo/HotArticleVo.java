package com.haue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class HotArticleVo {

    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 访问量
     */
    private Long viewCount;
    private Long createBy;


}