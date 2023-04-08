package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.entity.CommunityUser;
import com.haue.mapper.CommunityMapper;
import com.haue.pojo.entity.Community;
import com.haue.pojo.vo.HotCommentListVo;
import com.haue.pojo.vo.MyCommunityListVo;
import com.haue.service.CommunityService;
import com.haue.service.CommunityUserService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 社区信息表(Community)表服务实现类
 *
 * @author makejava
 * @since 2023-04-05 15:00:41
 */
@Service("communityService")
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {

    @Autowired
    private CommunityUserService communityUserService;

    /**
     * 查询热门社区
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult getHotCommunityList(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Community> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Community::getUserNumber);
        Page<Community> page = new Page<>(pageNum, pageSize);
        page(page,wrapper);
        List<Community> communityList = page.getRecords();
        return ResponseResult.okResult(BeanCopyUtils.copyBeanList(communityList, HotCommentListVo.class));
    }

    @Override
    public ResponseResult getMyCommunityList(Long id) {
        LambdaQueryWrapper<CommunityUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityUser::getUserId,id);
        List<CommunityUser> communityUserList = communityUserService.list(wrapper);
        if (communityUserList.size() < 1){
            return ResponseResult.okResult();
        }
        List<Long> communityList = communityUserList.stream()
                .map(communityUser -> communityUser.getCommunityId())
                .distinct()
                .collect(Collectors.toList());
        List<Community> communities = listByIds(communityList);
        return ResponseResult.okResult(BeanCopyUtils.copyBeanList(communities, MyCommunityListVo.class));
    }
}
