package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.params.RoleParam;
import com.haue.pojo.entity.Role;
import com.haue.utils.ResponseResult;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-04-17 15:24:57
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult getList(RoleParam roleDto);

    ResponseResult changeStatus(RoleParam roleDto);

    ResponseResult addRole(Role role);

    ResponseResult showRoleMsg(Long roleId);

    ResponseResult updateRole(Role role);

    ResponseResult deleteRole(Long roleId);

    ResponseResult getListAllRole();
}
