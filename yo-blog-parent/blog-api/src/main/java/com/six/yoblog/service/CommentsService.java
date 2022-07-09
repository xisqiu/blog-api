package com.six.yoblog.service;


import com.six.yoblog.vo.CommentVo;
import com.six.yoblog.vo.Result;

import java.util.List;

public interface CommentsService {
    Result commentsByarticleId(Long articleid);

    List<CommentVo> findcommentsByParentId(Long id);
}
