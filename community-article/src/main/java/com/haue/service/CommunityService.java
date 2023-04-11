package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.pojo.entity.Community;
import com.haue.utils.ResponseResult;


/**
 * 社区信息表(Community)表服务接口
 *
 * @author makejava
 * @since 2023-04-05 15:00:41
 */
public interface CommunityService extends IService<Community> {

    ResponseResult getHotCommunityList(Integer pageNum, Integer pageSize);

    ResponseResult getMyCommunityList(Long id);

    ResponseResult getRecommendCommunity(Integer pageNum, Integer pageSize);

    ResponseResult getCommunityInfo(Integer pageNum, Integer pageSize, Integer id);
}
