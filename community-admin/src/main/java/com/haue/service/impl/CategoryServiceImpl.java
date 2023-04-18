package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.constants.SystemConstants;
import com.haue.mapper.CategoryMapper;
import com.haue.params.CategoryParam;
import com.haue.pojo.entity.Category;
import com.haue.pojo.vo.PageVo;
import com.haue.service.CategoryService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import com.haue.vo.CategoryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 文章分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-04-18 16:42:11
 */
@Service("systemCategoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    /**
     * 后端系统
     * 写博文时显示所有正常状态分类
     * @return
     */
    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        List<Category> list = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    /**
     * 后端系统--分类管理
     * 查询分类信息
     * @param pageNum
     * @param pageSize
     * @param name
     * @param status
     * @return
     */
    @Override
    public ResponseResult getList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(name),Category::getName,name)
                .eq(StringUtils.hasText(status),Category::getStatus,status);
        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page,wrapper);

        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(page.getRecords(), CategoryVo.class);
        PageVo pageVo = new PageVo(categoryVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 后端系统--分类管理
     * 添加分类
     * @param category
     * @return
     */
    @Override
    public ResponseResult addCategory(Category category) {
        save(category);
        return ResponseResult.okResult();
    }

    /**
     * 后端系统--分类管理
     * 修改分类时显示当前分类的信息
     * @param id
     * @return
     */
    @Override
    public ResponseResult showCategoryInfo(Long id) {
        Category category = getById(id);
        CategoryVo categoryVo = BeanCopyUtils.copyBean(category, CategoryVo.class);
        return ResponseResult.okResult(categoryVo);
    }

    /**
     * 后端系统--分类管理
     * 更新分类信息
     * @param category
     * @return
     */
    @Override
    public ResponseResult updateCategory(Category category) {
        updateById(category);
        return ResponseResult.okResult();
    }

    /**
     * 后端系统--分类管理
     * 删除分类
     * @param id
     * @return
     */
    @Override
    public ResponseResult deleteCategory(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 修改分类状态
     * @param categoryParam
     * @return
     */
    @Override
    public ResponseResult changeStatus(CategoryParam categoryParam) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getId,categoryParam.getId());
        Category category = BeanCopyUtils.copyBean(categoryParam, Category.class);
        update(category,wrapper);
        return ResponseResult.okResult();
    }
}
