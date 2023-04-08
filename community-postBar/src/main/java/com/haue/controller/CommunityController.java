package com.haue.controller;

import com.haue.service.CommunityService;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/community")
@Validated
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @GetMapping("/hotCommunityList")
    public ResponseResult getHotCommunityList(@NotNull Integer pageNum ,@NotNull Integer pageSize){
        return communityService.getHotCommunityList(pageNum,pageSize);
    }

    @GetMapping("/myCommunityList/{id}")
    public ResponseResult getMyCommunityList(@NotNull( message = "myCommunityList请求：用户Id不能为空") @PathVariable("id") Long id){
        return communityService.getMyCommunityList(id);
    }
}
