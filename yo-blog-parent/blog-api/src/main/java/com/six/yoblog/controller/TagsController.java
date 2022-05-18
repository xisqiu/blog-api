package com.six.yoblog.controller;


import com.six.yoblog.service.TagService;
import com.six.yoblog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("tags")
public class TagsController {

    @Autowired
    TagService tagService;

    // 路径 /tags/hot
    // 定义6个最热标签
    @RequestMapping("/hot")
    public Result listHotTags(){
        int limit =6;
         return tagService.hots(limit);
    }
}
