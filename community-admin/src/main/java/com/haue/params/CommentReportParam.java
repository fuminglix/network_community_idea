package com.haue.params;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReportParam {

    @NotNull
    private Integer pageNum;
    @NotNull
    private Integer pageSize;
    @NotNull
    private Integer type;
    private String nickName;
    private String content;
}
