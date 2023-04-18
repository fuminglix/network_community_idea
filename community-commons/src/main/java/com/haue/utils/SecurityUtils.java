package com.haue.utils;

import com.haue.constants.SystemConstants;
import com.haue.pojo.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils
{

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        Object principal = getAuthentication().getPrincipal();
        return (LoginUser) principal;
        //        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin(){
        Long id = getLoginUser().getUser().getId();
        return id != null && id.equals(SystemConstants.ADMAIN_ID);
    }

    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }
}