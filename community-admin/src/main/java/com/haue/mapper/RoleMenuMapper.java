package com.haue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haue.pojo.entity.RoleMenu;
import org.springframework.stereotype.Repository;

/**
 * 角色和菜单关联表(RoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-18 10:15:04
 */
@Repository
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

}
