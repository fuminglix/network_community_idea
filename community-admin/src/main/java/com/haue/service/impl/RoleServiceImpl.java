package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.constants.SystemConstants;
import com.haue.mapper.RoleMapper;
import com.haue.mapper.RoleMenuMapper;
import com.haue.params.RoleParam;
import com.haue.pojo.entity.Role;
import com.haue.pojo.entity.RoleMenu;
import com.haue.pojo.vo.PageVo;
import com.haue.service.RoleMenuService;
import com.haue.service.RoleService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import com.haue.utils.SecurityUtils;
import com.haue.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-04-17 15:24:58
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    /**
     * 通过id查询用户角色信息
     * @param id
     * @return
     */
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        if (SecurityUtils.isAdmin()){
            List<String> list = new ArrayList<>();
            list.add("admin");
            return list;
        }
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    /**
     * 获取角色列表
     * @param roleDto
     * @return
     */
    @Override
    public ResponseResult getList(RoleParam roleDto) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(roleDto.getRoleName()),Role::getRoleName,roleDto.getRoleName())
                .eq(StringUtils.hasText(roleDto.getStatus()),Role::getStatus,roleDto.getStatus())
                .orderByAsc(Role::getRoleSort);

        Page<Role> page = new Page<>(roleDto.getPageNum(), roleDto.getPageSize());
        page(page,wrapper);

        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 点击状态按钮修改角色状态
     * @param roleDto
     * @return
     */
    @Override
    public ResponseResult changeStatus(RoleParam roleDto) {
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getId,roleDto.getRoleId());
        update(role,wrapper);
        return ResponseResult.okResult();
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @Override
    @Transactional
    public ResponseResult addRole(Role role) {
        save(role);
        if (role.getMenuIds().size()>0){
            List<RoleMenu> roleMenuList = role.getMenuIds().stream()
                    .map(m -> new RoleMenu(role.getId(), m))
                    .collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenuList);
        }
        return ResponseResult.okResult();
    }

    /**
     * 修改角色时显示当前角色的信息
     *
     * @param roleId
     * @return
     */
    @Override
    public ResponseResult showRoleMsg(Long roleId) {
        Role role = getById(roleId);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    /**
     * 更新角色信息
     * @param role
     * @return
     */
    @Override
    @Transactional
    public ResponseResult updateRole(Role role) {
        updateById(role);

        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,role.getId());
        roleMenuMapper.delete(wrapper);
        List<RoleMenu> menus = role.getMenuIds().stream()
                .map(m -> new RoleMenu(role.getId(), m))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(menus);
        return ResponseResult.okResult();
    }

    /**
     * 删除用户
     * @param roleId
     * @return
     */
    @Override
    public ResponseResult deleteRole(Long roleId) {
        removeById(roleId);
        return ResponseResult.okResult();
    }

    /**
     * 后端系统
     * 新增用户时显示所有状态正常的角色
     * @return
     */
    @Override
    public ResponseResult getListAllRole() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> roleList = list(wrapper);
        return ResponseResult.okResult(roleList);
    }
}
