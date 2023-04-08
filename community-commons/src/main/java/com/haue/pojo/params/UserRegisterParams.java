package com.haue.pojo.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterParams {
    //用户名
    @NotBlank
    private String userName;
    //昵称
    @NotBlank
    private String nickName;
    //密码
    @NotBlank
    private String password;
    @NotBlank
    //邮箱
    private String email;
}
