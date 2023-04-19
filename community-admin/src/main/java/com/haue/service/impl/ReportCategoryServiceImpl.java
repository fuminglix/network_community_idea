package com.haue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.mapper.ReportCategoryMapper;
import com.haue.pojo.entity.ReportCategory;
import com.haue.service.ReportCategoryService;
import org.springframework.stereotype.Service;

/**
 * 举报分类表(ReportCategory)表服务实现类
 *
 * @author makejava
 * @since 2023-04-19 15:13:27
 */
@Service("reportCategoryService")
public class ReportCategoryServiceImpl extends ServiceImpl<ReportCategoryMapper, ReportCategory> implements ReportCategoryService {

}
