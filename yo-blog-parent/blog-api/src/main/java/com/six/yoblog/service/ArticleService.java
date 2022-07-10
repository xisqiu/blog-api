package com.six.yoblog.service;

import com.six.yoblog.vo.ArticleVo;
import com.six.yoblog.vo.Result;
import com.six.yoblog.vo.params.ArticleParam;
import com.six.yoblog.vo.params.PageParams;

import java.util.List;

public interface ArticleService {

    /**
     * 分页查询 文章列表
     * @param pageParams
     * @return
     */
    List<ArticleVo> listArticle(PageParams pageParams);

    /**
     * 查询最热文章
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     * 查询最新文章
     * @param limit
     * @return
     */
    Result newArticles(int limit);

    /**
     * 文章归档
     * @param
     * @return
     */
    Result listArchives();

    /**
     *根据id 查询文章详情
     * @param id
     * @return
     */
    Result findArticleById(Long id);

    /**
     * 发布文章
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);
}
