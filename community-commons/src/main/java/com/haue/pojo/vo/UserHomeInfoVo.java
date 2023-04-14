package com.haue.pojo.vo;

import com.haue.pojo.entity.UserTotal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserHomeInfoVo {

    private Long id;
    //昵称
    private String nickName;
    //头像
    private String avatar;
    //简介
    private String profile;
    //主页背景图片
    private String backgroundImg;
    //性别
    private String sex;
    //用户的数据统计信息
    private UserTotal userTotal;
}
