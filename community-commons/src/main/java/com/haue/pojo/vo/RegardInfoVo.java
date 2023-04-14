package com.haue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RegardInfoVo {

    private List<AuthorInfoVo> regardList;

    private List<AuthorInfoVo> fanslist;
}
