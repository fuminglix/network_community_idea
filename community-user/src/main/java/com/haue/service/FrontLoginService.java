package com.haue.service;

import com.haue.pojo.entity.User;
import com.haue.pojo.params.UserCheckParam;
import com.haue.pojo.params.UserParam;
import com.haue.utils.ResponseResult;

public interface FrontLoginService {

    ResponseResult login(UserCheckParam userCheckParam);

    ResponseResult logout();

    ResponseResult updateUser(UserParam user);

    ResponseResult getUserInfo();
}
