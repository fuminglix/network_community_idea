package com.haue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haue.pojo.entity.UserTotal;
import org.springframework.stereotype.Repository;

/**
 * 用户数据统计信息表(UserTotal)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-01 19:15:36
 */
@Repository
public interface UserTotalMapper extends BaseMapper<UserTotal> {

}
