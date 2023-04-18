package com.haue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haue.pojo.entity.Category;
import org.springframework.stereotype.Repository;

/**
 * 文章分类表(Category)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-18 16:42:11
 */
@Repository
public interface CategoryMapper extends BaseMapper<Category> {

}
