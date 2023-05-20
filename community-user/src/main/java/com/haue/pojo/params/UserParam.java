package com.haue.pojo.params;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserParam {
    private Long id;

    //昵称
    private String nickName;
    //密码
    private String password;
    //邮箱
    private String email;
    //手机号
    private String phonenumber;
    //用户性别（0男，1女，2未知）
    private String sex;
    //头像
    private String avatar;
    //简介
    private String profile;
    //主页背景图片
    private String backgroundImg;
    //出生日期
    private Date birthday;
}
