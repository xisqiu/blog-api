package com.six.yoblog.controller;

import com.six.yoblog.dao.pojo.Article;
import com.six.yoblog.service.ArticleService;
import com.six.yoblog.vo.ArticleVo;
import com.six.yoblog.vo.params.PageParams;
import com.six.yoblog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// json 数据交互
@RestController
@RequestMapping(value = "articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    /**
     * 首页文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    public Result listArticle(@RequestBody PageParams pageParams){
        List<ArticleVo> articles = articleService.listArticle(pageParams);
        return Result.success(articles);
    }

    /**
     * 最热文章
     * @return
     */
    @PostMapping("hot")
    public Result hotArticle(){
        int limit = 3;
       return articleService.hotArticle(limit);
    }

    /**
     * 最新文章
     * @return
     */
    @PostMapping("new")
    public Result newArticles(){
        int limit = 2;
        return articleService.newArticles(limit);
    }

    /**
     * 最新文章
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

}
