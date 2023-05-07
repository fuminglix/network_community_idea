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
    //处理结果标志（0代表举报失败，1代表举报成功，2代表未处理）
    @NotNull
    private Integer repFlag;
    private Long update_by;
    //结果描述
    private String reportResult;
}
