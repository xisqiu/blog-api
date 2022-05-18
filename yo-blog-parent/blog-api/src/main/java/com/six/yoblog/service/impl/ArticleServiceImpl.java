package com.six.yoblog.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.six.yoblog.dao.dos.Archives;
import com.six.yoblog.dao.mapper.ArticleMapper;
import com.six.yoblog.dao.pojo.Article;
import com.six.yoblog.service.ArticleService;
import com.six.yoblog.service.SysUserService;
import com.six.yoblog.service.TagService;
import com.six.yoblog.vo.ArticleVo;
import com.six.yoblog.vo.Result;
import com.six.yoblog.vo.params.PageParams;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired(required = false)
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    /*
     * 1. 分页查询 artile文章列表
     */
    @Override
    public List<ArticleVo> listArticle(PageParams pageParams) {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Article.class);
        // 分页查询 article 数据库表
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPagesize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // Article::getCreateDate 是否按时间进行排序
        // Article::getWeight   是否置顶进行排序
        queryWrapper.orderByDesc(Article::getCreateDate, Article::getWeight);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
        // 能直接返回吗？不能 返回一个vo对象
        List<ArticleVo> articleVoList = copyList(records, true, true);
        return articleVoList;
    }


    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // 并不是所有接口都需要标签、作者信息
        if (isTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor) {
            Long authorid = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserByid(authorid).getNickname());
        }
        return articleVo;
    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        // select id,title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        // select id,title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }
}
