package com.haue.service;

import com.haue.pojo.params.UserCheckParam;
import com.haue.utils.ResponseResult;

public interface FrontLoginService {

    ResponseResult login(UserCheckParam userCheckParam);

    ResponseResult logout();
}
