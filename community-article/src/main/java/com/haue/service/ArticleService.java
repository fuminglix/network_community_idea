package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.pojo.entity.Article;
import com.haue.pojo.params.ArticleParam;
import com.haue.pojo.params.GetMyArticleParam;
import com.haue.pojo.params.SearchArticleParam;
import com.haue.utils.ResponseResult;


/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2023-03-28 15:41:00
 */
public interface ArticleService extends IService<Article> {

    ResponseResult getArticleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getHotArticleList();

    ResponseResult getArticleById(Long id,Boolean flag);

    ResponseResult addArticle(ArticleParam articleParam);

    ResponseResult getMyArticle(GetMyArticleParam myArticleParam);

    ResponseResult updateArticle(ArticleParam articleParam);

    ResponseResult searchArticle(SearchArticleParam param);

    ResponseResult getArticleList();

}
