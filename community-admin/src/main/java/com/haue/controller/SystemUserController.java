package com.haue.controller;


import com.haue.params.UserParam;
import com.haue.pojo.entity.User;
import com.haue.service.SystemUserService;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/system/user")
public class SystemUserController {

    @Autowired
    private SystemUserService systemUserService;

    @GetMapping("/list")
    public ResponseResult getList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status){
        return systemUserService.getList(pageNum,pageSize,userName,phonenumber,status);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody User user){
        return systemUserService.addUser(user);
    }

    @GetMapping("{id}")
    public ResponseResult showUserInfo(@PathVariable("id") Long id){
        return systemUserService.showUserInfo(id);
    }

    @PutMapping
    public ResponseResult updateUser(@RequestBody User user){
        return systemUserService.updateUser(user);
    }

    @DeleteMapping("{id}")
    public ResponseResult deleteUser(@PathVariable("id")Long id){
        return systemUserService.deleteUser(id);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody UserParam user){
        return systemUserService.changeStatus(user);
    }
}
