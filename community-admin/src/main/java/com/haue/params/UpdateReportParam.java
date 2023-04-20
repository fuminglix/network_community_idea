package com.haue.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReportParam {

    @NotNull
    private Long id;
    //举报分类名
    @NotNull
    private Integer repFlag;
    //举报描述
    private String reportResult;
}
