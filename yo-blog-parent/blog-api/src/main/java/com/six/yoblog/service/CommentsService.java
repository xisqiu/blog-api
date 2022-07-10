package com.six.yoblog.service;


import com.six.yoblog.vo.CommentVo;
import com.six.yoblog.vo.Result;
import com.six.yoblog.vo.params.CommentParam;

import java.util.List;

public interface CommentsService {
    Result commentsByarticleId(Long articleid);

    List<CommentVo> findcommentsByParentId(Long id);

    Result comments(CommentParam commentParam);
}
