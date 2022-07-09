package com.six.yoblog.controller;

import com.six.yoblog.service.CommentsService;
import com.six.yoblog.vo.Result;
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
}
