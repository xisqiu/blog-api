package com.six.yoblog.controller;

import com.six.yoblog.service.CommentsService;
import com.six.yoblog.vo.Result;
import com.six.yoblog.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("comments")
public class CommentsController {

    @Autowired(required = false)
    private CommentsService commentsService;

    /**
     * 根据articleid 查询
     */
    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long articleid) {
      return  commentsService.commentsByarticleId(articleid);
    }

    /**
     * 评论功能
     */
    @PostMapping("create/change")
    public Result comments(@RequestBody CommentParam commentParam){
        return commentsService.comments(commentParam);
    }
}
