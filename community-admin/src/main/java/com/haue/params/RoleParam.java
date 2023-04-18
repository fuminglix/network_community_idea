package com.haue.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleParam {
    private Integer roleId;
    private Integer pageNum;
    private Integer pageSize;
    //角色名称
    private String roleName;
    //角色状态（0正常 1停用）
    private String status;
}
