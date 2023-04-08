package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.pojo.entity.Category;
import com.haue.utils.ResponseResult;


/**
 * 文章分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-03-28 16:37:12
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}
