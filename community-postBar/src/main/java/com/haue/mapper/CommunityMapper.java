package com.haue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haue.pojo.entity.Community;
import org.springframework.stereotype.Repository;

/**
 * 社区信息表(Community)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-05 15:00:39
 */
@Repository
public interface CommunityMapper extends BaseMapper<Community> {

}
