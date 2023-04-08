package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.constants.SystemConstants;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.exception.SystemException;
import com.haue.mapper.ArticleMapper;
import com.haue.mapper.ArticleTagMapper;
import com.haue.mapper.TagMapper;
import com.haue.pojo.entity.Article;
import com.haue.pojo.entity.ArticleTag;
import com.haue.pojo.entity.Tag;
import com.haue.pojo.params.ArticleParam;
import com.haue.pojo.vo.ArticleDetailVo;
import com.haue.pojo.vo.ArticleListVo;
import com.haue.pojo.vo.HotArticleVo;
import com.haue.pojo.vo.PageVo;
import com.haue.service.ArticleService;
import com.haue.service.ArticleTagService;
import com.haue.service.CategoryService;
import com.haue.service.TagService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2023-03-28 15:41:01
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private ArticleTagMapper articleTagMapper;
    /**
     * 获取文章信息
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @Override
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getDelFlag, SystemConstants.ARTICLE_STATUS_NORMAL)
                .eq(Objects.nonNull(categoryId) && categoryId>0,Article::getCategoryId,categoryId)
                .orderByDesc(Article::getIsTop);
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page, wrapper);
        List<Article> articles = page.getRecords();

        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        //包装结果
        List<ArticleListVo> vos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        PageVo pageVo = new PageVo(vos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 查询热门文章
     * @return
     */
    @Override
    public ResponseResult getHotArticleList() {
//        查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                .orderByDesc(Article::getViewCount);
        Page<Article> page = new Page<>(1, 5);
        page(page,wrapper);
        List<Article> articles = page.getRecords();
        return ResponseResult.okResult(BeanCopyUtils.copyBeanList(articles, HotArticleVo.class));
    }

    /**
     * 通过id查询文章详情
     * @param id 文章id
     * @return ArticleDetailVo封装的数据
     */
    @Override
    public ResponseResult getArticleById(Long id) {

        Article article = getById(id);
        article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());

        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagMapper.selectList(wrapper);
        if(articleTags.size() > 0){
            List<Long> tags = articleTags.stream()
                    .map(a -> a.getTagId())
                    .distinct()
                    .collect(Collectors.toList());
            article.setTags(tagMapper.selectBatchIds(tags));
        }
        return ResponseResult.okResult(BeanCopyUtils.copyBean(article, ArticleDetailVo.class));
    }

    /**
     * 添加文章
     * @param articleParam
     * @return
     */
    @Override
    @Transactional
    public ResponseResult addArticle(ArticleParam articleParam) {
        Article article = BeanCopyUtils.copyBean(articleParam, Article.class);
        save(article);
//        添加文章标签
        if (articleParam.getTags().size() > 0){
            ArrayList<ArticleTag> articleTags = new ArrayList<>();

            List<Tag> tagList = BeanCopyUtils.copyBeanList(articleParam.getTags(), Tag.class);
            for ( Tag tag : tagList){
                ArticleTag articleTag = new ArticleTag( article.getId(),0L);
                LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Tag::getTagName,tag.getTagName());
                Tag exist = tagService.getOne(wrapper);
//                Tag exist = tagMapper.selectOne(wrapper);
//                判断tag表中是否已存在该标签
                if (exist != null){
//                    若存在复用该id
                    articleTag.setTagId(exist.getId());
                }else{
//                    若不存在将该标签添加到tag表，然后再向articleTag的tagId注入值
                    tagService.save(tag);
                    articleTag.setTagId(tag.getId());
                }
                articleTags.add(articleTag);
            }
//            将articleTag添加到表中
            articleTagService.saveBatch(articleTags);
            return ResponseResult.okResult();
//            List<ArticleTag> articleTags = articleParam.getTags().stream()
//                    .map(tag -> new ArticleTag(article.getId(), tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getTagName, tag.getTagName())).getId()))
//
//                    .collect(Collectors.toList());
//            articleTags.stream()
//                    .map(articleTag -> articleTag.getTagId() != null ? articleTag : tagService.save(articleParam.getTags().stream().flatMap(tag -> tag.getId())) )
        }

        return ResponseResult.okResult();
    }
}
