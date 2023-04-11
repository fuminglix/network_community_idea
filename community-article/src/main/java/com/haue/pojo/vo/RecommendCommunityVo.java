package com.haue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendCommunityVo {

    private MyCommunityListVo community;

    private RecommendArticleVo article;

    private AuthorInfoVo user;
}
