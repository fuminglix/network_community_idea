package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.exception.SystemException;
import com.haue.mapper.UserMapper;
import com.haue.pojo.entity.User;
import com.haue.pojo.params.SearchUserParam;
import com.haue.pojo.params.UserRegisterParams;
import com.haue.pojo.vo.AuthorInfoVo;
import com.haue.service.UserService;
import com.haue.service.UserTotalService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 网站用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-03-27 09:08:15
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserTotalService userTotalService;
    @Override
    public ResponseResult register(UserRegisterParams userParams) {
        User user = BeanCopyUtils.copyBean(userParams, User.class);
        userInfoExist(user, null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        return ResponseResult.okResult();
    }

    /**
     * 查询文章作者信息
     * @param id 文章id
     * @return
     */
    @Override
    public ResponseResult getAuthorInfo(Long id) {
        User user = getById(id);
        AuthorInfoVo infoVo = BeanCopyUtils.copyBean(user, AuthorInfoVo.class);
        infoVo.setUserTotal(userTotalService.getById(id));
        return ResponseResult.okResult(infoVo);
    }

    /**
     * 搜索用户
     * @param param
     * @return
     */
    @Override
    public ResponseResult searchUser(SearchUserParam param) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Objects.nonNull(param.getSearch()),User::getUserName,param.getSearch());
        List<User> userList = list(wrapper).stream()
                .distinct()
                .collect(Collectors.toList());
        return ResponseResult.okResult(userList);
    }

    /**
     * 判断用户信息是否已存在
     * @param user
     * @param id
     * @return
     */
    private boolean userInfoExist(User user,Long id){
        //对数据进行非空判断
//        if (id == null){
//            if(!StringUtils.hasText(user.getUserName())){
//                throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
//            }
//            if(!StringUtils.hasText(user.getPassword())){
//                throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
//            }
//            if(!StringUtils.hasText(user.getEmail())){
//                throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
//            }
//            if(!StringUtils.hasText(user.getNickName())){
//                throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
//            }
//        }
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
