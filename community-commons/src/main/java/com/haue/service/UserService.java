package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.pojo.entity.User;
import com.haue.pojo.params.SearchUserParam;
import com.haue.pojo.params.UserRegisterParams;
import com.haue.utils.ResponseResult;


/**
 * 网站用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-03-27 09:08:15
 */
public interface UserService extends IService<User> {

    ResponseResult register(UserRegisterParams userParams);

    ResponseResult getAuthorInfo(Long id);

    ResponseResult searchUser(SearchUserParam param);

    ResponseResult getActivityInfo(Long id);

    ResponseResult getUserInfo();

    ResponseResult getRegardInfo(Integer pageSize, Integer pageNum);
}
