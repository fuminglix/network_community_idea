package com.haue.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ActivityVo {

    private Long id;
    //动态内容
    private String content;
    //转发时添加的内容
    private String dispatchContent;
    //动态图片
    private List<String> contentImg;
}
