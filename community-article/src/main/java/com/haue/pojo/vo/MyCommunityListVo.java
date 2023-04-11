package com.haue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyCommunityListVo {

    //社区id
    private Long id;
    //社区名
    private String communityName;
    //社区头像
    private String avatar;
}
