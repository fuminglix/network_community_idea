package com.haue.pojo.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserCheckParam {
    //用户名
    @NotBlank
    private String userName;
    //密码
    @NotBlank
    private String password;
}
