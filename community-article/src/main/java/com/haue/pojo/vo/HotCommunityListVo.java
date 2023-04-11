package com.haue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotCommunityListVo {
    //社区id
    private Long id;

    //社区名
    private String communityName;
    //社区头像
    private String avatar;
    //社区用户数
    private Long userNumber;
    //社区发布内容数
    private Long contentNumber;
    //创建用户id
    private Long createBy;
}
