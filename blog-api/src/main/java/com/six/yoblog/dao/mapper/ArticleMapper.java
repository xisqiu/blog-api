package com.six.yoblog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.six.yoblog.dao.dos.Archives;
import com.six.yoblog.dao.pojo.Article;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {
    List<Archives> listArchives();
}
