package com.haue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haue.pojo.entity.Article;
import org.springframework.stereotype.Repository;


/**
 * 文章表(Article)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-28 15:44:02
 */
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

}
