package com.haue.pojo.params;

import com.haue.pojo.entity.Img;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddActivityContentParam {

    //动态内容
    @NotBlank
    private String content;
    //转发标志（0代表非转发 1转发文章 2转发动态）
    private String isRef;
    //转发的文章或者动态id
    private Long refId;
    //转发时添加的内容
    private String dispatchContent;
    //社区id
    private Long communityId;
    //动态图片
    private List<Img> imgList;
}
