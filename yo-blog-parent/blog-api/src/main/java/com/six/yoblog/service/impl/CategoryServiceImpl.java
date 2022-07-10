package com.six.yoblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.six.yoblog.dao.mapper.CategoryMapper;
import com.six.yoblog.dao.pojo.Category;
import com.six.yoblog.service.CategoryService;
import com.six.yoblog.vo.CategoryVo;
import com.six.yoblog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Result findAll() {
        List<Category> categories = this.categoryMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(copylist(categories));
    }

    private List<Category> copylist(List<Category> categoryList) {
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList){
            categoryVoList.add(copy(category));
        }
        return categoryList;
    }

    private CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return  categoryVo;
    }
}
