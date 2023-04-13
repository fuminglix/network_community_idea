package com.haue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityInfoVo {

    private AuthorInfoVo authorInfoVo;

    private PageVo pageVo;
}
