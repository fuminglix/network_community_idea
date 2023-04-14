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

    /**
     * 获取热门社区列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/hotCommunityList")
    public ResponseResult getHotCommunityList(@NotNull Integer pageNum ,@NotNull Integer pageSize){
        return communityService.getHotCommunityList(pageNum,pageSize);
    }

    /**
     * 获取当前用户关注的社区列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/myCommunityList")
    public ResponseResult getMyCommunityList(Integer pageNum,Integer pageSize){
        return communityService.getMyCommunityList(pageNum,pageSize);
    }

    /**
     * 获取社区主页推荐的社区列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/recommendCommunity")
    public ResponseResult getRecommendCommunity(@NotNull Integer pageNum ,@NotNull Integer pageSize){
        return communityService.getRecommendCommunity(pageNum,pageSize);
    }

    /**
     * 获取当前社区详情
     * @param pageNum
     * @param pageSize
     * @param communityId
     * @return
     */
    @GetMapping("/CommunityInfo")
    public ResponseResult getCommunityInfo(@NotNull Integer pageNum ,@NotNull Integer pageSize,@NotNull Long communityId){
        return communityService.getCommunityInfo(pageNum,pageSize,communityId);
    }

    /**
     * 查询当前用户在其关注社区的动态信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/regardCommunityInfo")
    public ResponseResult getRegardCommunityInfo(@NotNull Integer pageNum ,@NotNull Integer pageSize){
        return communityService.getRegardCommunityInfo(pageNum,pageSize);
    }

}
