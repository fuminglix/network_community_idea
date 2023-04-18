package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.params.CategoryParam;
import com.haue.pojo.entity.Category;
import com.haue.utils.ResponseResult;


/**
 * 文章分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-04-18 16:42:11
 */
public interface CategoryService extends IService<Category> {

    ResponseResult listAllCategory();

    ResponseResult getList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(Category category);

    ResponseResult showCategoryInfo(Long id);

    ResponseResult updateCategory(Category category);

    ResponseResult deleteCategory(Long id);

    ResponseResult changeStatus(CategoryParam categoryParam);
}
