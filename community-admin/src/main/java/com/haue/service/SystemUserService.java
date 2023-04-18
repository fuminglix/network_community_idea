package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.params.UserParam;
import com.haue.pojo.entity.User;
import com.haue.utils.ResponseResult;


/**
 * 网站用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-04-18 14:57:58
 */
public interface SystemUserService extends IService<User> {

    ResponseResult getList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addUser(User user);

    ResponseResult showUserInfo(Long id);

    ResponseResult updateUser(User user);

    ResponseResult deleteUser(Long id);

    ResponseResult changeStatus(UserParam user);
}
