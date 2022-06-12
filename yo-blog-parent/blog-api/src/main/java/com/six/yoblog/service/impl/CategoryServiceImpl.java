package com.six.yoblog.service.impl;

import com.six.yoblog.dao.mapper.CategoryMapper;
import com.six.yoblog.dao.pojo.Category;
import com.six.yoblog.service.CategoryService;
import com.six.yoblog.vo.CategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CategoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;
    
    @Override
    public CategoryVo findCategoryByid(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}
