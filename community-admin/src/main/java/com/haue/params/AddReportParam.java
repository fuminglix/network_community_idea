package com.haue.params;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AddReportParam {
    private Long id;

    //被举报内容id
    @NotNull
    private Long contentId;
    //举报的内容类型（0 文章，1 评论，2 动态）
    @NotNull
    private Integer type;
    //被举报用户id
    private Long reportedId;
    //举报人
    private Long createBy;
    //举报分类id
    @NotNull
    private Integer reportCategory;
    //举报描述
    private String reportDescription;
}
