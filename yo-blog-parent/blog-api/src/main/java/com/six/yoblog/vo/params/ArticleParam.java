package com.six.yoblog.vo.params;

import com.six.yoblog.vo.CategoryVo;
import com.six.yoblog.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}