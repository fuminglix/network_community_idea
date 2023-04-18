package com.haue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haue.pojo.entity.User;
import org.springframework.stereotype.Repository;

/**
 * 网站用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-18 14:57:48
 */
@Repository
public interface SystemUserMapper extends BaseMapper<User> {

}
