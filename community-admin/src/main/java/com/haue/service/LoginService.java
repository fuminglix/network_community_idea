package com.haue.service;

import com.haue.pojo.entity.User;
import com.haue.utils.ResponseResult;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
