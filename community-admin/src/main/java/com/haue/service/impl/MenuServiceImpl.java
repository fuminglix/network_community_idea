package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.constants.SystemConstants;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.exception.SystemException;
import com.haue.mapper.MenuMapper;
import com.haue.pojo.entity.Menu;
import com.haue.pojo.entity.RoleMenu;
import com.haue.service.MenuService;
import com.haue.service.RoleMenuService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import com.haue.utils.SecurityUtils;
import com.haue.vo.MenuTreeVo;
import com.haue.vo.MenuVo;
import com.haue.vo.RoleMenuTreeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-04-17 15:25:17
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuService roleMenuService;


    /**
     * 通过id查询用户权限
     * @param id
     * @return
     */
    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if (SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON)
                    .eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menuList = list(wrapper);
            List<String> list = menuList.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return list;
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    /**
     * 获取包含父子关系的目录信息
     * @param userId
     * @return
     */
    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if (SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else {
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    /**
     * 获取菜单列表信息
     * @param menuName
     * @param status
     * @return
     */
    @Override
    public ResponseResult getList(String menuName, String status) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName)
                .like(StringUtils.hasText(status),Menu::getStatus,status)
                .orderByAsc(Menu::getOrderNum,Menu::getParentId);
        List<Menu> menuList = list(wrapper);
        return ResponseResult.okResult(menuList);
    }

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    /**
     * 更新菜单时显示当前菜单的信息
     * @param id
     * @return
     */
    @Override
    public ResponseResult showMenuInfo(Long id) {
        Menu menu = getById(id);
        MenuVo menuVo = BeanCopyUtils.copyBean(menu, MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    /**
     * 更新菜单
     * @param newMenu 前端传递的更新数据
     * @return
     */
    @Override
    public ResponseResult updateMenu(Menu newMenu) {
//        查询当前菜单的子菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,newMenu.getId());
        List<Menu> menuChildrenList = list(wrapper);

        List<Long> pIdList = new ArrayList<>();
//        判断当前菜单是否有子菜单
        if (menuChildrenList.size()>0){
//            获取当前菜单所有子菜单的id
            pIdList = getParentIdById(newMenu.getId(),pIdList);
        }
//        获取已选父菜单信息
        Menu menuNew = getById(newMenu.getParentId());
//        获取当前菜单信息
        Menu menuOld = getById(newMenu.getId());
//        筛选当前菜单的子菜单中是否包含已选父菜单
        List<Long> pIdCollect = pIdList.stream()
                .filter(menuId -> newMenu.getParentId().equals(menuId))
                .collect(Collectors.toList());
//        判断已选父菜单是否为当前菜单 或 包含在当前菜单的子菜单中
        if ( (Objects.nonNull(menuNew) && Objects.equals(menuNew.getParentId(), menuOld.getParentId())) || pIdCollect.size()>0){
//            若满足以上条件之一则抛出异常
            throw new SystemException(AppHttpCodeEnum.MENU_ERROR);
        }
//        更新信息无误 执行
        updateById(newMenu);
        return ResponseResult.okResult();
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @Override
    public ResponseResult deleteMenu(Long id) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,id);
        List<Menu> menuList = list(wrapper);
        if (menuList.size()>0){
            throw new SystemException(AppHttpCodeEnum.MENU_CHILDREN_EXIST);
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 新增角色时需查询的菜单信息
     * @return
     */
    @Override
    public ResponseResult treeSelect() {
//        查询菜单信息，以parentId升序排列
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Menu::getParentId);
        List<Menu> menuList = list(wrapper);
//        将查询到的数据包装为MenuTreeVo类型
        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(menuList, MenuTreeVo.class);
//        把数据构建成树形
        List<MenuTreeVo> menuTreeVoList = builderMenuTreeVo(menuList,menuTreeVos,0L);

        return ResponseResult.okResult(menuTreeVoList);
    }

    /**
     * 修改角色信息时加载对应角色菜单列表树
     * @param roleId
     * @return
     */
    @Override
    public ResponseResult roleMenuTreeSelect(Long roleId) {
//      调用 treeSelect() 方法查询所有菜单并以树形返回
        ResponseResult treeSelect = treeSelect();
        List<MenuTreeVo> treeSelectData = (List<MenuTreeVo>) treeSelect.getData();

        List<Long> menus = null;
//        如果是超级管理员直接返回所有菜单
        if (SystemConstants.ADMAIN_ID.equals(roleId)){
            menus = list().stream()
                    .map(Menu::getId)
                    .distinct()
                    .collect(Collectors.toList());
        }else {
//          查询当前角色拥有的菜单权限
//          将菜单对应的 id 封装成集合
            LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(RoleMenu::getRoleId,roleId);
            menus = roleMenuService.list(wrapper).stream()
                    .map(RoleMenu::getMenuId)
                    .collect(Collectors.toList());
        }
//      将数据封装成指定格式返回
        RoleMenuTreeVo roleMenuTreeVo = new RoleMenuTreeVo(treeSelectData, menus);
        return ResponseResult.okResult(roleMenuTreeVo);
    }

    /**
     * 构建菜单树
     * @param menus
     * @param parentId
     * @return
     */
    private List<Menu> builderMenuTree(List<Menu> menus, long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取父菜单的子菜单
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> children = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(m.getChildren()))
                .collect(Collectors.toList());

        return children;
    }

    /**
     * 获取父菜单的所有子菜单id
     * @param id
     * @param pIdList
     * @return
     */
    private List<Long> getParentIdById(Long id, List<Long> pIdList) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,id);
        List<Menu> menuList = list(wrapper);
        if (menuList.size() != 0){
            for (Menu menu : menuList) {
                pIdList.add(menu.getId());
                getParentIdById(menu.getId(),pIdList);
            }
        }
        return pIdList;
    }

    /**
     * 把 Menu 类的数据构建成 MenuTreeVo 类的数据
     * @param menus
     * @param menuTreeVos
     * @param parentId
     * @return
     */
    private List<MenuTreeVo> builderMenuTreeVo(List<Menu> menus, List<MenuTreeVo> menuTreeVos, long parentId) {
//        调用 getLabel() 给 menuTreeVos 中的 label 字段赋值
        List<MenuTreeVo> menuTreeVoList = menuTreeVos.stream()
                .map(menuTreeVo -> menuTreeVo.setLabel(getLabel(menus,menuTreeVo.getId())))
                .collect(Collectors.toList());
//        调用 getMenuTreeVoChildren() 方法给 menuTreeVos 中的 children 字段赋值
        List<MenuTreeVo> menuTree = menuTreeVoList.stream()
                .filter(menuTreeVo -> menuTreeVo.getParentId().equals(parentId))
                .map(menuTreeVo -> menuTreeVo.setChildren(getMenuTreeVoChildren(menuTreeVos,menuTreeVo.getId())))
                .collect(Collectors.toList());

        return menuTree;
    }

    /**
     * 获取 Menu 类中对应的 MenuName 字段信息
     * 用于赋值给 MenuTreeVo 类中对应的 label 字段
     * @param menus
     * @param id
     * @return
     */
    private String getLabel(List<Menu> menus, Long id) {
        List<String> stringList = menus.stream()
                .filter(menu -> menu.getId().equals(id))
                .map(Menu::getMenuName)
                .collect(Collectors.toList());

        return stringList.get(0);
    }

    /**
     * 获取 MenuTreeVo 类的 children 字段的数据
     * @param menuTreeVos
     * @param parentId
     * @return
     */
    private List<MenuTreeVo> getMenuTreeVoChildren(List<MenuTreeVo> menuTreeVos, Long parentId) {
        List<MenuTreeVo> menuTreeVoList = menuTreeVos.stream()
                .filter(menuTreeVo -> menuTreeVo.getParentId().equals(parentId))
//                递归设置子菜单的子菜单
                .map(menuTreeVo -> menuTreeVo.setChildren(getMenuTreeVoChildren(menuTreeVos,menuTreeVo.getId())))
                .collect(Collectors.toList());

        return menuTreeVoList;
    }
}
