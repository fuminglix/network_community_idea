package com.haue.controller;


import com.haue.enums.AppHttpCodeEnum;
import com.haue.pojo.entity.User;
import com.haue.pojo.params.UserCheckParam;
import com.haue.pojo.params.UserParam;
import com.haue.service.FrontLoginService;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class FrontLoginController {

    @Autowired
    private FrontLoginService frontLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody @Validated UserCheckParam userCheckParam, BindingResult result){
        if (result.hasErrors()){
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        return frontLoginService.login(userCheckParam);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return frontLoginService.logout();
    }

    @PutMapping("/userInfo")
    public ResponseResult updateUser(@RequestBody UserParam user){
        return frontLoginService.updateUser(user);
    }

    @GetMapping("/userInfo")
    public ResponseResult getUserInfo(){
        return frontLoginService.getUserInfo();
    }
}
