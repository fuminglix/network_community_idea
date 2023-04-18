package com.haue.controller;

import com.haue.params.RoleParam;
import com.haue.pojo.entity.Role;
import com.haue.service.RoleService;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult getList(RoleParam roleDto){
        return roleService.getList(roleDto);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleParam roleDto){
        return roleService.changeStatus(roleDto);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody Role role){
        return roleService.addRole(role);
    }

    @GetMapping("{id}")
    public ResponseResult showRoleMsg(@PathVariable("id") Long id){
        return roleService.showRoleMsg(id);
    }

    @PutMapping
    public ResponseResult updateRole(@RequestBody Role role){
        return roleService.updateRole(role);
    }

    @DeleteMapping("{id}")
    public ResponseResult deleteRole(@PathVariable("id") Long roleId){
        return roleService.deleteRole(roleId);
    }

    @GetMapping("/listAllRole")
    public ResponseResult getListAllRole(){
        return roleService.getListAllRole();
    }
}
