package com.haue.pojo.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryParam {
    @NotNull
    private Integer pageNum;
    @NotNull
    private Integer pageSize;
    @NotNull
    private Long categoryId;
}
