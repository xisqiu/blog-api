package com.six.yoblog.service;

import com.six.yoblog.vo.CategoryVo;

public interface CategoryService  {
    CategoryVo findCategoryByid(Long categoryId);
}
