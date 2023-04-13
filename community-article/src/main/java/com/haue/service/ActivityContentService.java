package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.pojo.entity.ActivityContent;
import com.haue.pojo.params.AddActivityContentParam;
import com.haue.utils.ResponseResult;


/**
 * (ActivityContent)表服务接口
 *
 * @author makejava
 * @since 2023-04-12 10:42:57
 */
public interface ActivityContentService extends IService<ActivityContent> {

    ResponseResult getActivityInfo(Integer pageNum,Integer pageSize, Long id);

    ResponseResult addActivityContent(AddActivityContentParam activityContentParam);
}
