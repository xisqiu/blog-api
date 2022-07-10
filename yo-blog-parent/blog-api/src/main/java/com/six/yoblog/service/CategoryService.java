package com.six.yoblog.service;

import com.six.yoblog.vo.CategoryVo;
import com.six.yoblog.vo.Result;

public interface CategoryService  {
    CategoryVo findCategoryByid(Long categoryId);

    Result findAll();

}
