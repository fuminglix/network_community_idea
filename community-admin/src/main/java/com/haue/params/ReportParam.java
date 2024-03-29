package com.haue.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportParam {

    @NotNull
    private Integer pageNum;
    @NotNull
    private Integer pageSize;
    @NotNull
    private Integer type;
    private Long articleId;
    private String title;
    private String nickName;
    private String content;

}
