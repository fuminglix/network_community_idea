package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.exception.SystemException;
import com.haue.mapper.SystemUserMapper;
import com.haue.mapper.UserRoleMapper;
import com.haue.params.UserParam;
import com.haue.pojo.entity.Role;
import com.haue.pojo.entity.User;
import com.haue.pojo.entity.UserRole;
import com.haue.pojo.vo.PageVo;
import com.haue.service.RoleService;
import com.haue.service.UserRoleService;
import com.haue.service.SystemUserService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import com.haue.utils.SecurityUtils;
import com.haue.vo.ShowUserInfoVo;
import com.haue.vo.UserListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 网站用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-04-18 14:57:59
 */
@Service("systemUserService")
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, User> implements SystemUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 获取用户列表
     * @param pageNum
     * @param pageSize
     * @param userName
     * @param phonenumber
     * @param status
     * @return
     */
    @Override
    public ResponseResult getList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(userName),User::getUserName,userName)
                .eq(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber)
                .eq(StringUtils.hasText(status),User::getStatus,status);

        Page<User> page = new Page<>(pageNum, pageSize);
        page(page,wrapper);

        PageVo pageVo = new PageVo(BeanCopyUtils.copyBeanList(page.getRecords(), UserListVo.class), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 后端系统
     * 添加用户
     * @param user
     * @return
     */
    @Override
    @Transactional
    public ResponseResult addUser(User user) {
        userInfoExist(user,null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);

        List<Long> roleIds = user.getRoleIds();
        List<UserRole> userRoleList = roleIds.stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
        return ResponseResult.okResult();
    }

    /**
     * 后端系统
     * 修改用户信息时回显的信息
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ResponseResult showUserInfo(Long id) {
//        获取用户信息
        User user = getById(id);
//        获取所有状态正常的角色信息
        List<Role> roles = (List<Role>) roleService.getListAllRole().getData();
//        获取当前用户的角色信息
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoles = userRoleService.list(wrapper);
        List<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
//        将所有信息封装成指定格式
        ShowUserInfoVo showUserInfoVo = new ShowUserInfoVo(roleIds, roles, user);
        return ResponseResult.okResult(showUserInfoVo);
    }

    /**
     * 后端系统
     * 修改用户信息
     * @param user
     * @return
     */
    @Override
    @Transactional
    public ResponseResult updateUser(User user) {
//        判断修改的信息中是否有数据库中已存在的数据
        userInfoExist(user, user.getId());
//        更新用户信息
        updateById(user);
//        删除UserRole中对应用户的信息
//        userRoleMapper.deleteRoleId(user.getId());
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,user.getId());
        userRoleMapper.delete(wrapper);
//        判断修改的信息中是否有用户角色信息
        if (user.getRoleIds().size()>0){
//        查询当前用户的角色信息
            List<UserRole> userRoles = user.getRoleIds().stream()
                    .map(roleId -> new UserRole(user.getId(), roleId))
                    .collect(Collectors.toList());
//        将修改后的用户角色信息存储到UserRole表中
            userRoleService.saveBatch(userRoles);
        }
        return ResponseResult.okResult();
    }

    /**
     * 后端系统
     * 删除用户
     * @param id
     * @return
     */
    @Override
    public ResponseResult deleteUser(Long id) {
        if (id == null){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        if (SecurityUtils.getUserId().equals(id)){
            throw new SystemException(AppHttpCodeEnum.ERROR_DELETE);
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 后端系统
     * 修改用户状态
     * @param userDto
     * @return
     */
    @Override
    public ResponseResult changeStatus(UserParam userDto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId,userDto.getUserId());
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        update(user,wrapper);
        return ResponseResult.okResult();
    }

    /**
     * 判断用户信息是否已存在
     * @param user
     * @param id
     * @return
     */
    private boolean userInfoExist(User user,Long id){
        //对数据进行非空判断
        if (id == null){
            if(!StringUtils.hasText(user.getUserName())){
                throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
            }
            if(!StringUtils.hasText(user.getPassword())){
                throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
            }
            if(!StringUtils.hasText(user.getEmail())){
                throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
            }
            if(!StringUtils.hasText(user.getNickName())){
                throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
            }
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName(),id)){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName(),id)){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(phoneNumberExist(user.getPhonenumber(),id)){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        return false;
    }

    /**
     * 判断用户昵称是否存在
     * @param nickName
     * @param id
     * @return
     */
    private boolean nickNameExist(String nickName,Long id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName)
                .notIn(id != null,User::getId,id);
        return count(queryWrapper)>0;
    }

    /**
     * 判断用户名是否存在
     * @param userName
     * @param id
     * @return
     */
    private boolean userNameExist(String userName,Long id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName)
                .notIn(id != null,User::getId,id);
        return count(queryWrapper)>0;
    }

    /**
     * 判断电话号码是否已存在
     * @param phoneNumber
     * @param id
     * @return
     */
    private boolean phoneNumberExist(String phoneNumber,Long id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber,phoneNumber)
                .notIn(id != null,User::getId,id);
        return count(queryWrapper)>0;
    }
}
