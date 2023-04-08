package com.haue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haue.pojo.entity.User;
import org.springframework.stereotype.Repository;


/**
 * 网站用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-27 09:08:14
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

}
