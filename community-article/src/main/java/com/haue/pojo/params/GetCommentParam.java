package com.haue.pojo.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentParam {

    @NotNull
    private Long articleId;
    @NotNull
    private Integer pageNum;
    @NotNull
    private Integer pageSize;
}
