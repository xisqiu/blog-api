package com.six.yoblog.service;

import com.six.yoblog.vo.Result;
import com.six.yoblog.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);
}
