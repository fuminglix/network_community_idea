package com.haue.pojo.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoveParam {
    private Long id;

    //点赞内容id
    private Long contentId;
    //被点赞用户id
    private Long contentCreateBy;
    //点赞内容类型（0 文章，1内容）
    private Integer type;

}
