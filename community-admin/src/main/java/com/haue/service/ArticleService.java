package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.params.AddArticleParam;
import com.haue.params.ArticleListParam;
import com.haue.pojo.entity.Article;
import com.haue.utils.ResponseResult;
import com.haue.vo.ArticleUpdateVo;


/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2023-04-18 16:35:04
 */
public interface ArticleService extends IService<Article> {

    ResponseResult add(AddArticleParam article);

    ResponseResult getList(ArticleListParam articleListDto);

    ResponseResult showArticleInfo(Long id);

    ResponseResult updateArticle(ArticleUpdateVo article);

    ResponseResult deleteArticle(Long id);
}
