package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.constants.SystemConstants;
import com.haue.mapper.ArticleMapper;
import com.haue.mapper.ArticleTagMapper;
import com.haue.mapper.TagMapper;
import com.haue.pojo.entity.*;
import com.haue.pojo.params.ArticleParam;
import com.haue.pojo.params.GetMyArticleParam;
import com.haue.pojo.params.SearchArticleParam;
import com.haue.pojo.vo.*;
import com.haue.service.*;
import com.haue.utils.*;
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

    @Autowired
    private CheckResultService checkResultService;

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
                .eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL)
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
    public ResponseResult getArticleById(Long id,Boolean flag) {

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
        if (flag){
            return ResponseResult.okResult(article);
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
        save(article); //保存文章
        //审核内容
        CheckResult result = CheckContentUtil.check(articleParam.getContent());
        if (SystemConstants.CHECK_SUCCESS_CODE != result.getSuggest()){ //审核不通过
            result.setType(SystemConstants.CHECK_ARTICLE);
            result.setContentId(article.getId());
            checkResultService.save(result); //插入审核记录
            update(null,new LambdaUpdateWrapper<Article>() //根据审核结果修改文章状态
                    .eq(Article::getId,article.getId())
                    .set(Article::getStatus,SystemConstants.ARTICLE_STATUS_REVIEW_FAILED));
        }else { //审核通过
            result.setType(SystemConstants.CHECK_ARTICLE);
            result.setContentId(article.getId());
            checkResultService.save(result); //插入审核记录
            update(null,new LambdaUpdateWrapper<Article>() //根据审核结果修改文章状态
                    .eq(Article::getId,article.getId())
                    .set(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL));
        }
//        添加文章标签
        if (articleParam.getTags().size() > 0){
            return addAndUpdateTags(article,articleParam);
//            ArrayList<ArticleTag> articleTags = new ArrayList<>();
//            List<Tag> tagList = BeanCopyUtils.copyBeanList(articleParam.getTags(), Tag.class);
//            for ( Tag tag : tagList){
//                ArticleTag articleTag = new ArticleTag( article.getId(),0L);
//                LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
//                wrapper.eq(Tag::getTagName,tag.getTagName());
//                Tag exist = tagService.getOne(wrapper);
////                Tag exist = tagMapper.selectOne(wrapper);
////                判断tag表中是否已存在该标签
//                if (exist != null){
////                    若存在复用该id
//                    articleTag.setTagId(exist.getId());
//                }else{
////                    若不存在将该标签添加到tag表，然后再向articleTag的tagId注入值
//                    tagService.save(tag);
//                    articleTag.setTagId(tag.getId());
//                }
//                articleTags.add(articleTag);
//            }
////            将articleTag添加到表中
//            articleTagService.saveBatch(articleTags);
//            return ResponseResult.okResult();
//            List<ArticleTag> articleTags = articleParam.getTags().stream()
//                    .map(tag -> new ArticleTag(article.getId(), tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getTagName, tag.getTagName())).getId()))
//
//                    .collect(Collectors.toList());
//            articleTags.stream()
//                    .map(articleTag -> articleTag.getTagId() != null ? articleTag : tagService.save(articleParam.getTags().stream().flatMap(tag -> tag.getId())) )
        }

        return ResponseResult.okResult();
    }

    /**
     * 获取当前用户的所有文章
     * @param param
     * @return
     */
    @Override
    public ResponseResult getMyArticle(GetMyArticleParam param) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getCreateBy,param.getUserId())
                .notIn(Objects.nonNull(param.getStatus()) && SystemConstants.DEFAULT_STATUS.equals(param.getStatus()),Article::getStatus,SystemConstants.ARTICLE_STATUS_DRAFT)
                .eq(Objects.nonNull(param.getStatus()) && SystemConstants.ARTICLE_STATUS_DRAFT.equals(param.getStatus()),Article::getStatus,param.getStatus())
                .eq(Objects.nonNull(param.getStatus()) && !SystemConstants.DEFAULT_STATUS.equals(param.getStatus()) && !SystemConstants.ARTICLE_STATUS_DRAFT.equals(param.getStatus()),Article::getStatus,param.getStatus())
                .orderBy(Objects.nonNull(param.getOrderBy()) && SystemConstants.ARTICLE_STATUS_DRAFT.equals(param.getOrderBy()),false,Article::getCreateTime)
                .orderBy(Objects.nonNull(param.getOrderBy()) && SystemConstants.ARTICLE_STATUS_REVIEW.equals(param.getOrderBy()),false,Article::getViewCount)
                .orderBy(Objects.nonNull(param.getOrderBy()) && SystemConstants.ARTICLE_STATUS_REVIEW_FAILED.equals(param.getOrderBy()),false,Article::getCommentCount)
                .orderBy(Objects.nonNull(param.getOrderBy()) && SystemConstants.ARTICLE_STATUS_SCHEDULED.equals(param.getOrderBy()),false,Article::getCollectCount)
                .like(Objects.nonNull(param.getSearch()),Article::getTitle,param.getSearch());
        Page<Article> page = new Page<>(param.getPageNum(),param.getPageSize());
        page(page,wrapper);
        List<MyArticleVo> myArticleVos = BeanCopyUtils.copyBeanList(page.getRecords(), MyArticleVo.class);
        return ResponseResult.okResult(new PageVo(myArticleVos,page.getTotal()));
    }

    /**
     * 更新文章
     * @param articleParam
     * @return
     */
    @Override
    @Transactional
    public ResponseResult updateArticle(ArticleParam articleParam) {
        Article article = BeanCopyUtils.copyBean(articleParam, Article.class);
        updateById(article);

        //        添加文章标签
        if (articleParam.getTags().size() > 0){
            return addAndUpdateTags(article,articleParam);
        }
        return ResponseResult.okResult();
    }

    /**
     * 搜索文章
     * @param param
     * @return
     */
    @Override
    public ResponseResult searchArticle(SearchArticleParam param) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Objects.nonNull(param.getSearch()),Article::getTitle,param.getSearch());
        List<Article> articleList = list(wrapper).stream()
                .distinct()
                .collect(Collectors.toList());
        return ResponseResult.okResult(articleList);
    }

    /**
     * 获取当前用户主页的文章列表
     * @return
     */
    @Override
    public ResponseResult getArticleList() {
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getCreateBy,userId)
                .eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        Page<Article> page = new Page<>(SystemConstants.DEFAULT_PAGE_NUM, SystemConstants.DEFAULT_ARTICLE_PAGE_SIZE);
        page(page,wrapper);
        return ResponseResult.okResult(new PageVo(BeanCopyUtils.copyBeanList(page.getRecords(),ArticleListVo.class), page.getTotal()));
    }

    /**
     * 添加标签
     * @param article
     * @param articleParam
     * @return
     */
    public ResponseResult addAndUpdateTags(Article article,ArticleParam articleParam){
        ArrayList<ArticleTag> articleTags = new ArrayList<>();
        List<Tag> tagList = BeanCopyUtils.copyBeanList(articleParam.getTags(), Tag.class);
        for ( Tag tag : tagList){
            ArticleTag articleTag = new ArticleTag( article.getId(),0L);
            LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Tag::getTagName,tag.getTagName());
            Tag exist = tagService.getOne(wrapper);
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
        ArrayList<ArticleTag> articleTagList = new ArrayList<>();
        for ( ArticleTag articleTag : articleTags ) {
            LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ArticleTag::getArticleId,articleTag.getArticleId())
                    .eq(ArticleTag::getTagId,articleTag.getTagId());
            ArticleTag tag = articleTagService.getOne(wrapper);
            if (tag == null){
                articleTagList.add(articleTag);
            }
        }
//        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
//        List<ArticleTag> articleTagList = articleTags.stream()
//                .filter(articleTag -> articleTagService.getOne(wrapper
//                        .eq(ArticleTag::getArticleId,articleTag.getArticleId()).eq(ArticleTag::getTagId,articleTag.getTagId())) == null)
//                .collect(Collectors.toList());
        if (articleTagList.size()>0){
            articleTagService.saveBatch(articleTagList);
        }
        return ResponseResult.okResult();
    }


}
