package com.haue.controller;


import com.haue.enums.AppHttpCodeEnum;
import com.haue.pojo.params.UserCheckParam;
import com.haue.service.FrontLoginService;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
