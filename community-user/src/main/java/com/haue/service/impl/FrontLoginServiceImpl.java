package com.haue.service.impl;

import com.haue.constants.SystemConstants;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.exception.SystemException;
import com.haue.pojo.params.UserCheckParam;
import com.haue.pojo.entity.LoginUser;
import com.haue.pojo.vo.BlogUserLoginVo;
import com.haue.pojo.vo.UserInfoVo;
import com.haue.service.FrontLoginService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.JwtUtil;
import com.haue.utils.RedisCache;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FrontLoginServiceImpl implements FrontLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    private LoginUser loginUser;

    /**
     * 前端用户登录
     * @param user
     * @return
     */
    @Override
    public ResponseResult login(UserCheckParam user) {
        //进行信息验证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
        //获取userid 生成token
        try{
            loginUser = (LoginUser) authenticate.getPrincipal();
        }catch (Exception e){
            return ResponseResult.okResult(new SystemException(AppHttpCodeEnum.TOKEN_NOT_EXIT));
        }
        //LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject(SystemConstants.REDIS_LOGIN_KEY + userId, loginUser);

        //把token和userinfo封装 返回
        //把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt, userInfoVo);
        loginUser = null;
        return ResponseResult.okResult(vo);
    }

    /**
     * 前端用户退出登录
     * @return
     */
    @Override
    public ResponseResult logout() {
        //获取token 解析获取userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取userid
        //Object principal = authentication.getPrincipal();
        try{
            loginUser = (LoginUser) authentication.getPrincipal();
        }catch (Exception e){
            return ResponseResult.okResult(new SystemException(AppHttpCodeEnum.TOKEN_NOT_EXIT));
        }
        Long userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject(SystemConstants.REDIS_LOGIN_KEY + userId);
        loginUser = null;
        return ResponseResult.okResult();
    }
}
