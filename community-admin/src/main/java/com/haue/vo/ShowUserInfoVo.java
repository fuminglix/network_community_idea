package com.haue.vo;

import com.haue.pojo.entity.Role;
import com.haue.pojo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowUserInfoVo {

    private List<Long> roleIds;
    private List<Role> roles;
    private User user;
}
