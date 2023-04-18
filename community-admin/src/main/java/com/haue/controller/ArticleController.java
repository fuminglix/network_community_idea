package com.haue.controller;

import com.haue.params.AddArticleParam;
import com.haue.params.ArticleListParam;
import com.haue.service.ArticleService;
import com.haue.utils.ResponseResult;
import com.haue.vo.ArticleUpdateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleParam article){
        return articleService.add(article);
    }

    @GetMapping("/list")
    public ResponseResult getList(ArticleListParam articleListDto){
        return articleService.getList(articleListDto);
    }

    @GetMapping("{id}")
    public ResponseResult showArticleInfo(@PathVariable("id")Long id){
        return articleService.showArticleInfo(id);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody ArticleUpdateVo article){
        return articleService.updateArticle(article);
    }

    @DeleteMapping("{id}")
    public ResponseResult deleteArticle(@PathVariable("id") Long id){
        return articleService.deleteArticle(id);
    }
}
