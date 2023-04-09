package com.haue.controller;


import com.haue.enums.AppHttpCodeEnum;
import com.haue.pojo.params.ArticleParam;
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

    @PostMapping
    public ResponseResult addArticle(@RequestBody @Validated ArticleParam articleParam, BindingResult result){
        if (result.hasErrors()){
//            return ResponseResult.errorResult(AppHttpCodeEnum.INFO_NOT_NULL);
            return ResponseResult.errorResult(600,result.toString());
        }
        return articleService.addArticle(articleParam);
    }

    @GetMapping("/articleList")
    public ResponseResult getArticleList(Integer pageNum,Integer pageSize,Long categoryId){
//        if (!StringUtils.hasText(pageNum.toString()) || !StringUtils.hasText(pageSize.toString()) || !StringUtils.hasText(categoryId.toString())){
//                throw new SystemException(AppHttpCodeEnum.PARAMS_NOT_NULL);
//        }
        return articleService.getArticleList(pageNum,pageSize,categoryId);
    }

    @GetMapping("/hotArticleList")
    public ResponseResult getHotArticleList(){
        return articleService.getHotArticleList();
    }

    @GetMapping("/getArticle/{articleId}")
    public ResponseResult getArticleById(@PathVariable("articleId") @NotNull(message = "文章id不能为空！") Long id){
        return articleService.getArticleById(id);
    }

    @GetMapping("/category")
    public ResponseResult getCategoryList(){
        return categoryService.getCategoryList();
    }

    /**
     * 获取当前用户的所有文章
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    @GetMapping("/manage/articleList")
    public ResponseResult getMyArticle(@NotNull(message = "pageNum不能为空") Integer pageNum,
                                       @NotNull(message = "pageSize不能为空") Integer pageSize,
                                       @NotNull(message = "userId不能为空") Long userId){
        return articleService.getMyArticle(pageNum,pageSize,userId);
    }
}
