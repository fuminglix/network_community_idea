package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.mapper.CommunityMapper;
import com.haue.pojo.entity.Article;
import com.haue.pojo.entity.Community;
import com.haue.pojo.entity.CommunityUser;
import com.haue.pojo.entity.User;
import com.haue.pojo.vo.*;
import com.haue.service.ArticleService;
import com.haue.service.CommunityService;
import com.haue.service.CommunityUserService;
import com.haue.service.UserService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

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
        return ResponseResult.okResult(BeanCopyUtils.copyBeanList(communityList, HotCommunityListVo.class));
    }

    /**
     * 获取当前用户关注的社区列表
     * @param id
     * @return
     */
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

    /**
     * 获取社区主页推荐的社区列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult getRecommendCommunity(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Community> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Community::getActivity);
        Page<Community> page = new Page<>(pageNum, pageSize);
        page(page,wrapper);
        List<Community> communityList = page.getRecords();
        ArrayList<RecommendCommunityVo> arrayList = new ArrayList<>();
        for (Community community : communityList) {
            LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Article::getCommunityId,community.getId())
                    .orderByDesc(Article::getViewCount);
            List<Article> list = articleService.list(queryWrapper);
            if (list.size() > 0){
                RecommendCommunityVo vo = new RecommendCommunityVo();
                vo.setCommunity(BeanCopyUtils.copyBean(community,MyCommunityListVo.class));
                vo.setArticle(BeanCopyUtils.copyBean(list.get(0), RecommendArticleVo.class));
                User user = userService.getById(list.get(0).getCreateBy());
                vo.setUser(BeanCopyUtils.copyBean(user, AuthorInfoVo.class));
                arrayList.add(vo);
            }
        }
        return ResponseResult.okResult(new PageVo(arrayList,page.getTotal()));
    }

    /**
     * 获取当前社区详情
     * @param pageNum
     * @param pageSize
     * @param id
     * @return
     */
    @Override
    public ResponseResult getCommunityInfo(Integer pageNum, Integer pageSize, Integer id) {

        return ResponseResult.okResult();
    }
}
