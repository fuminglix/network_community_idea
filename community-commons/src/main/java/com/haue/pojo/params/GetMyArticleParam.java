package com.haue.pojo.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GetMyArticleParam {

    @NotNull(message = "pageNum不能为空")
    private Integer pageNum;
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
    @NotNull(message = "userId不能为空")
    private Long userId;
    @NotBlank(message = "status不能为空")
    private String status;
    @NotBlank(message = "orderBy不能为空")
    private String orderBy;
    @NotBlank(message = "orderBy不能为空")
    private String search;
}
