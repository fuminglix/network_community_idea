package com.haue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haue.pojo.entity.Report;
import org.springframework.stereotype.Repository;

/**
 * 被举报文章表(ArticleReport)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-19 15:13:37
 */
@Repository
public interface ReportMapper extends BaseMapper<Report> {

}
