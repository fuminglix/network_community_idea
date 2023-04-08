package com.haue.controller;


import com.haue.enums.AppHttpCodeEnum;
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

    @PostMapping("/register")
    public ResponseResult register(@RequestBody @Validated UserRegisterParams userParams, BindingResult result){
        if (result.hasErrors()){
            return ResponseResult.errorResult(AppHttpCodeEnum.INFO_NOT_NULL);
        }
        return userService.register(userParams);
    }

    @GetMapping("/getAuthorInfo/{authorId}")
    public ResponseResult getAuthorInfo(@PathVariable("authorId") Long id){
        return userService.getAuthorInfo(id);
    }
}

