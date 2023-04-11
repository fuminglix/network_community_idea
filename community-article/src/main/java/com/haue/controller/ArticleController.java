package com.haue.controller;


import com.haue.enums.AppHttpCodeEnum;
import com.haue.pojo.params.ArticleParam;
import com.haue.pojo.params.GetMyArticleParam;
import com.haue.pojo.params.SearchArticleParam;
import com.haue.service.ArticleService;
import com.haue.service.CategoryService;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 添加文章
     * @param articleParam
     * @param result
     * @return
     */
    @PostMapping
    public ResponseResult addArticle(@RequestBody @Validated ArticleParam articleParam, BindingResult result){
        if (result.hasErrors()){
//            return ResponseResult.errorResult(AppHttpCodeEnum.INFO_NOT_NULL);
            return ResponseResult.errorResult(600,result.toString());
        }
        return articleService.addArticle(articleParam);
    }

    /**
     * 更新文章
     * @param articleParam
     * @param result
     * @return
     */
    @PutMapping
    public ResponseResult updateArticle(@RequestBody @Validated ArticleParam articleParam, BindingResult result){
        if (result.hasErrors()){
//            return ResponseResult.errorResult(AppHttpCodeEnum.INFO_NOT_NULL);
            return ResponseResult.errorResult(600,result.toString());
        }
        return articleService.updateArticle(articleParam);
    }

    /**
     * 获取所有文章
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @GetMapping("/articleList")
    public ResponseResult getArticleList(Integer pageNum,Integer pageSize,Long categoryId){
//        if (!StringUtils.hasText(pageNum.toString()) || !StringUtils.hasText(pageSize.toString()) || !StringUtils.hasText(categoryId.toString())){
//                throw new SystemException(AppHttpCodeEnum.PARAMS_NOT_NULL);
//        }
        return articleService.getArticleList(pageNum,pageSize,categoryId);
    }

    /**
     * 获取热门文章
     * @return
     */
    @GetMapping("/hotArticleList")
    public ResponseResult getHotArticleList(){
        return articleService.getHotArticleList();
    }

    /**
     * 获取当前文章详情
     * @param id
     * @return
     */
    @GetMapping("/getArticle/{articleId}")
    public ResponseResult getArticleById(@PathVariable("articleId") @NotNull(message = "文章id不能为空！") Long id){
        return articleService.getArticleById(id,false);
    }

    /**
     * 获取当前编辑文章详情
     * @param id
     * @return
     */
    @GetMapping("/getUpdateArticle/{articleId}")
    public ResponseResult getUpdateArticle(@PathVariable("articleId") @NotNull(message = "文章id不能为空！") Long id){
        return articleService.getArticleById(id,true);
    }

    /**
     * 搜索文章
     * @param param
     * @param result
     * @return
     */
    @GetMapping("/search")
    public ResponseResult searchArticle(SearchArticleParam param,BindingResult result){
        if (result.hasErrors()){
            return ResponseResult.errorResult(600,result.toString());
        }
        return articleService.searchArticle(param);
    }

    /**
     * 获取分类列表
     * @return
     */
    @GetMapping("/category")
    public ResponseResult getCategoryList(){
        return categoryService.getCategoryList();
    }

    /**
     * 获取当前用户的所有文章
     * @param myArticleParam
     * @param result
     * @return
     */
    @GetMapping("/manage/articleList")
    public ResponseResult getMyArticle(GetMyArticleParam myArticleParam,BindingResult result){
        return articleService.getMyArticle(myArticleParam);
    }
}
