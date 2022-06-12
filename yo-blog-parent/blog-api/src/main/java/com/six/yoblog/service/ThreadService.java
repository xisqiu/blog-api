package com.six.yoblog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.six.yoblog.dao.mapper.ArticleMapper;
import com.six.yoblog.dao.pojo.Article;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    // 希望此操作在线程池 执行 不会影响原有的主线程
   @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article){
       int viewCounts = article.getViewCounts();
       Article articleUpdate = new Article();
       articleUpdate.setViewCounts(viewCounts +1 );
       LambdaQueryWrapper<Article> updateWrapper = new LambdaQueryWrapper<>();
       updateWrapper.eq(Article::getId,article.getId());
       // 设置一个 为了在多线程环境下 线程安全
       updateWrapper.eq(Article::getViewCounts,viewCounts);
       // update article set view_count=100 where view_count=99 and id=11
       articleMapper.update(articleUpdate,updateWrapper);
       try {
               Thread.sleep(5000);
               System.out.println("update completed......");
           } catch (InterruptedException e) {
               e.printStackTrace();
           }

   }
}
