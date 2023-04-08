package com.haue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.mapper.CategoryMapper;
import com.haue.pojo.entity.Category;
import com.haue.pojo.vo.CategoryListVo;
import com.haue.service.CategoryService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-03-28 16:37:12
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    /**
     * 查询分类信息
     * @return 分类信息列表
     */
    @Override
    public ResponseResult getCategoryList() {
        List<Category> categoryList = list();
        return ResponseResult.okResult(BeanCopyUtils.copyBeanList(categoryList, CategoryListVo.class));
    }
}
