package com.haue.pojo.vo;

import com.haue.pojo.entity.UserTotal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorInfoVo {
    private Long id;

    private String nickName;
    private String avatar;
    //简介
    private String profile;
    //用户数据统计
    private UserTotal userTotal;

}
