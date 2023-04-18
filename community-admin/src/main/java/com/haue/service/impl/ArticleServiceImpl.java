package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.exception.SystemException;
import com.haue.mapper.ArticleMapper;
import com.haue.params.AddArticleParam;
import com.haue.params.ArticleListParam;
import com.haue.pojo.entity.Article;
import com.haue.pojo.entity.ArticleTag;
import com.haue.pojo.vo.PageVo;
import com.haue.service.ArticleService;
import com.haue.service.ArticleTagService;
import com.haue.service.CategoryService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.RedisCache;
import com.haue.utils.ResponseResult;
import com.haue.vo.ArticleUpdateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2023-04-18 16:35:04
 */
@Service("systemArticleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    /**
     * 添加博文
     * @param articleDto
     * @return
     */
    @Override
    @Transactional
    public ResponseResult add(AddArticleParam articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
//        向article表中添加博文
        save(article);

        List<ArticleTag> articleTagList = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
//        向article_tag表中添加数据
        articleTagService.saveBatch(articleTagList);
        return ResponseResult.okResult();
    }

    /**
     * 获取文章列表信息
     * @param articleListDto
     * @return
     */
    @Override
    public ResponseResult getList(ArticleListParam articleListDto) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(articleListDto.getTitle()),Article::getTitle,articleListDto.getTitle())
                .like(StringUtils.hasText(articleListDto.getSummary()),Article::getSummary,articleListDto.getSummary());
        Page<Article> page = new Page<>(articleListDto.getPageNum(), articleListDto.getPageSize());
        page(page,wrapper);
        return ResponseResult.okResult(new PageVo(page.getRecords(),page.getTotal()));
    }

    /**
     * 展示想要修改文章的信息
     * @param id
     * @return
     */
    @Override
    public ResponseResult showArticleInfo(Long id) {
        Article article = getById(id);
//        查询文章对应的tags
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> tagsList = articleTagService.list(wrapper);
        ArticleUpdateVo articleUpdateVo = BeanCopyUtils.copyBean(article, ArticleUpdateVo.class);
        List<Long> tags = null;
        if (tagsList.size() > 0){
            tags = tagsList.stream()
                    .map(ArticleTag -> ArticleTag.getTagId())
                    .collect(Collectors.toList());
        }
        articleUpdateVo.setTags(tags);
        return ResponseResult.okResult(articleUpdateVo);
    }

    /**
     * 更新文章信息
     * @param articleUpdateVo
     * @return
     */
    @Override
    @Transactional
    public ResponseResult updateArticle(ArticleUpdateVo articleUpdateVo) {
        Article article = BeanCopyUtils.copyBean(articleUpdateVo, Article.class);
        updateById(article);
//        删除articleTag表中对应articleId的标签
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(wrapper);
//        判断标签是否为空
        if (articleUpdateVo.getTags().size()>0){
//            拿到标签集合
            List<ArticleTag> tagList = articleUpdateVo.getTags().stream()
                    .map(tagId -> new ArticleTag(article.getId(), tagId))
                    .collect(Collectors.toList());
//            保存到表中
            articleTagService.saveBatch(tagList);
        }
        return ResponseResult.okResult();
    }

    /**
     * 删除文章
     * @param id
     * @return
     */
    @Override
    public ResponseResult deleteArticle(Long id) {
        if (id == null){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        removeById(id);
        return ResponseResult.okResult();
    }
}
