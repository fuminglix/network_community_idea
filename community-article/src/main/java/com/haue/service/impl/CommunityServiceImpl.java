package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.constants.SystemConstants;
import com.haue.mapper.CommunityMapper;
import com.haue.pojo.entity.*;
import com.haue.pojo.vo.*;
import com.haue.service.*;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Autowired
    private ActivityContentService activityContentService;

    @Autowired
    private ImgService imgService;

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
                vo.setArticle(BeanCopyUtils.copyBean(list.get(0), RecommendArticleVo.class).setUser(BeanCopyUtils.copyBean(userService.getById(list.get(0).getCreateBy()), AuthorInfoVo.class)));
                arrayList.add(vo);
            }
        }
        return ResponseResult.okResult(new PageVo(arrayList,page.getTotal()));
    }

    /**
     * 获取当前社区详情
     * @param pageNum 页码
     * @param pageSize 页数
     * @param id 社区id
     * @return
     */
    @Override
    public ResponseResult getCommunityInfo(Integer pageNum, Integer pageSize, Long id) {
        //查询社区信息
        Community community = getById(id);
        LambdaQueryWrapper<ActivityContent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityContent::getCommunityId,id);
        //查询动态内容
        List<ActivityContent> activityContents = activityContentService.list(wrapper);
        //将动态的作者信息和转发的文章、作者信息封装
        List<ActivityContentVo> contents = BeanCopyUtils.copyBeanList(activityContents, ActivityContentVo.class).stream()
                .map(content -> SystemConstants.IS_REF.equals(content.getIsRef()) ? //判断是不是转发内容
                        content.setArticleVo //将转发的文章信息封装到ArticleVo
                                (BeanCopyUtils.copyBean(articleService.getById(content.getRefId()), RecommendArticleVo.class).setUser //将转发文章的作者信息封装到ArticleVo中
                                        (BeanCopyUtils.copyBean(userService.getById(articleService.getById(content.getRefId()).getCreateBy()), AuthorInfoVo.class))) : content)
                .map(content -> content.setUser(BeanCopyUtils.copyBean(userService.getById(content.getCreateBy()), AuthorInfoVo.class))) //将动态的作者信息封装到ActivityContentVo中
                .collect(Collectors.toList());

        //将动态的图片封装到ActivityContentVo中
        for (ActivityContentVo content : contents) {
            List<Img> list = imgService.list(new LambdaQueryWrapper<Img>().eq(Img::getActivityId, content.getId()));
            if (Objects.nonNull(list)){
                List<String> urls = list.stream()
                        .map(Img::getUrl)
                        .distinct()
                        .collect(Collectors.toList());
                content.setContentImg(urls);
            }
        }
//        contents = contents.stream()
//                .map(content -> content.getContentImg().addAll(imgService.list(new LambdaQueryWrapper<Img>().eq(Img::getActivityId, content.getId())).stream().map(img -> img.getUrl()).collect(Collectors.toList())))
//                .collect(Collectors.toList());

        return ResponseResult.okResult(new CommunityInfoVo(community,contents));
    }
}
