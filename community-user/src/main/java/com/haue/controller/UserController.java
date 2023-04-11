package com.haue.controller;


import com.haue.enums.AppHttpCodeEnum;
import com.haue.pojo.params.SearchUserParam;
import com.haue.pojo.params.UserRegisterParams;
import com.haue.service.UserService;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 网站用户表(User)表控制层
 *
 * @author fml
 * @since 2023-03-27 09:08:13
 */
@RestController
@RequestMapping("/user")
public class UserController{

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param userParams
     * @param result
     * @return
     */
    @PostMapping("/register")
    public ResponseResult register(@RequestBody @Validated UserRegisterParams userParams, BindingResult result){
        if (result.hasErrors()){
            return ResponseResult.errorResult(AppHttpCodeEnum.INFO_NOT_NULL);
        }
        return userService.register(userParams);
    }

    /**
     * 获取文章作者信息
     * @param id
     * @return
     */
    @GetMapping("/getAuthorInfo/{authorId}")
    public ResponseResult getAuthorInfo(@PathVariable("authorId") Long id){
        return userService.getAuthorInfo(id);
    }

    /**
     * 搜索用户
     * @param param
     * @param result
     * @return
     */
    @GetMapping("/search")
    public ResponseResult searchUser(SearchUserParam param,BindingResult result){
        if (result.hasErrors()){
            return ResponseResult.errorResult(600,result.toString());
        }
        return userService.searchUser(param);
    }
}

