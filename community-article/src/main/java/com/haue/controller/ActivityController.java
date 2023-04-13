package com.haue.controller;

import com.haue.pojo.params.AddActivityContentParam;
import com.haue.service.ActivityContentService;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@Validated
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityContentService activityContentService;

    /**
     * 查询当前用的动态信息
     * @param userId
     * @return
     */
    @GetMapping("/activityInfo")
    public ResponseResult getActivityInfo(@NotNull Integer pageNum,@NotNull Integer pageSize, @NotNull Long userId){
        return activityContentService.getActivityInfo(pageNum,pageSize,userId);
    }

    @PostMapping
    public ResponseResult addActivityContent(@RequestBody AddActivityContentParam activityContentParam){
        return activityContentService.addActivityContent(activityContentParam);
    }
}
