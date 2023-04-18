package com.haue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haue.pojo.entity.UserRole;
import org.springframework.stereotype.Repository;

/**
 * 用户和角色关联表(UserRole)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-18 15:05:44
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
