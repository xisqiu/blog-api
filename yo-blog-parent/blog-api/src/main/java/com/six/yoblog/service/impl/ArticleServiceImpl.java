package com.six.yoblog.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.six.yoblog.dao.dos.Archives;
import com.six.yoblog.dao.mapper.ArticleBodyMapper;
import com.six.yoblog.dao.mapper.ArticleMapper;
import com.six.yoblog.dao.mapper.ArticleTagMapper;
import com.six.yoblog.dao.pojo.*;
import com.six.yoblog.service.*;
import com.six.yoblog.until.UserThreadLocal;
import com.six.yoblog.vo.ArticleBodyVo;
import com.six.yoblog.vo.ArticleVo;
import com.six.yoblog.vo.Result;
import com.six.yoblog.vo.TagVo;
import com.six.yoblog.vo.params.ArticleParam;
import com.six.yoblog.vo.params.PageParams;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired(required = false)
    private ArticleTagMapper articleTagMapper;

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
            articleVoList.add(copy(record, isTag, isAuthor,false,false));
        }
        return articleVoList;
    }

    // 由于copylist 用于了其他地方，下面对copulist方法进行重载，重新传参
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor,boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
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
        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryByid(categoryId));
        }
        return articleVo;
    }

    @Autowired
    private CategoryService categoryService;


    @Autowired(required = false)
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
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

    @Autowired
    private ThreadService threadService;

    @Override
    public Result findArticleById(Long articleId){
        /**
         * 1.根据id查询文章信息
         * 2.根据bodyid 和 categoryid 去做关联查询
         */
        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true,true,true);
        /**
         * 查看完文章后，新增阅读数，
         * 查看文章后，本应该返回数据了,这时候做了一个更新操作，更新时间加写锁，阻塞其他的读操作，性能就会比较低了
         * 更新 增加了此次接口的耗时，如果一旦更新出现问题，不能影响 查看文章的操作
         * 线程池 可以把更新操作 扔到线程池中去执行和主线程就不相关了
         */
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo) ;
    }

    @Override
    @Transactional
    public Result publish(ArticleParam articleParam) {
        // 从线程中获取缓存的用户信息，发布前进行登录验证
        SysUser sysUser = UserThreadLocal.get();
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        this.articleMapper.insert(article);

        List<TagVo> tags = new ArrayList<>();
        if (tags != null){
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                this.articleTagMapper.insert(articleTag);
            }
        }

        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);

        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);
    }
}
