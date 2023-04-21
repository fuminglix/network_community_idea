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
import com.haue.utils.SecurityUtils;
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
 * TODO 转发动态时要确认被转发的动态是否也是转发动态，若是需要将被转发的dispatchContent与当前转发用户的dispatchContent拼接（一般用 // 隔开）
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
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult getMyCommunityList(Integer pageNum, Integer pageSize) {
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<CommunityUser> wrapper = new LambdaQueryWrapper<>();
        Page<CommunityUser> page = new Page<>();
        wrapper.eq(CommunityUser::getUserId,userId);
        if (Objects.nonNull(pageNum) && Objects.nonNull(pageSize)){
            page.setCurrent(pageNum);
            page.setSize(pageSize);
        }else {
            page.setCurrent(SystemConstants.DEFAULT_PAGE_NUM);
            page.setSize(SystemConstants.DEFAULT_COMMUNITY_PAGE_SIZE);
        }
        communityUserService.page(page,wrapper);
        List<CommunityUser> communityUserList = page.getRecords();
        if (communityUserList.size() < 1){
            return ResponseResult.okResult();
        }
        List<Long> communityList = communityUserList.stream()
                .map(CommunityUser::getCommunityId)
                .distinct()
                .collect(Collectors.toList());
        List<Community> communities = listByIds(communityList);
        return ResponseResult.okResult(new PageVo(BeanCopyUtils.copyBeanList(communities, MyCommunityListVo.class),page.getTotal()));
    }

    /**
     * 获取社区主页推荐的社区列表
     * @param pageNum
     * @param pageSize
     * @return
     * TODO 社区的推荐的内容默认是从文章中找，不合适，社区动态不能发文章，只能转发文章。前端将QuoteItem换成PostbarItem组件
     *
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
                .map(content -> SystemConstants.IS_REF_ARTICLE.equals(content.getIsRef()) ? //判断是不是转发内容
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
        return ResponseResult.okResult(new CommunityInfoVo(community,contents));
    }

    /**
     * 查询当前用户在其关注社区的动态信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult getRegardCommunityInfo(Integer pageNum, Integer pageSize) {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //查询当前用在关注社区发布的动态
        LambdaQueryWrapper<ActivityContent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityContent::getCreateBy,userId)
                .notIn(ActivityContent::getCommunityId,SystemConstants.DEFAULT_COMMUNITY_ID)
                .orderByDesc(ActivityContent::getCreateTime);
        Page<ActivityContent> page = new Page<>(pageNum, pageSize);
        activityContentService.page(page, wrapper);
        //封装查询到的动态信息
        List<ActivityContentVo> activityContentVos = toActivityContentVo(page.getRecords());
        return ResponseResult.okResult(new PageVo(activityContentVos,page.getTotal()));
    }

    /**
     * 处理动态中的图片和部分转发动态
     * @param activityContents
     * @return
     */
    public List<ActivityContentVo> toActivityContentVo(List<ActivityContent> activityContents) {
        //将动态的作者信息和转发的文章、作者信息封装
        List<ActivityContentVo> contents = BeanCopyUtils.copyBeanList(activityContents, ActivityContentVo.class).stream()
                .peek(content -> {
                    content.setUser(BeanCopyUtils.copyBean(userService.getById(content.getCreateBy()), AuthorInfoVo.class));
                    if (SystemConstants.IS_REF_ARTICLE.equals(content.getIsRef())){
                        content.setArticleVo(BeanCopyUtils.copyBean(articleService.getById(content.getRefId()), RecommendArticleVo.class).setUser //将转发文章的作者信息封装到ArticleVo中
                                (BeanCopyUtils.copyBean(userService.getById(articleService.getById(content.getRefId()).getCreateBy()), AuthorInfoVo.class)));
                    }
                    if (SystemConstants.IS_REF_ACTIVITY.equals(content.getIsRef())){
                        RecommendArticleVo articleVo = new RecommendArticleVo();
                        ActivityContentVo activityContentVo = BeanCopyUtils.copyBean(getById(content.getRefId()), ActivityContentVo.class);
                        List<Img> imgs = imgService.list(new LambdaQueryWrapper<Img>().eq(Img::getActivityId, content.getRefId()));
                        activityContentVo.setContentImg(getImgToString(imgs));

                        articleVo.setActivityContentVo(activityContentVo);
                        content.setArticleVo(articleVo.setUser(BeanCopyUtils.copyBean(userService.getById(getById(content.getRefId()).getCreateBy()), AuthorInfoVo.class)));
                    }
                    List<Img> list = imgService.list(new LambdaQueryWrapper<Img>().eq(Img::getActivityId, content.getId()));
                    content.setContentImg(getImgToString(list));
                })
                .collect(Collectors.toList());
        return contents;
    }

    /**
     * 将img对象的url属性以String集合的形式返回
     * @param imgs
     * @return
     */
    public List<String> getImgToString(List<Img> imgs){
        List<String> urls = new ArrayList<>();
        if (Objects.nonNull(imgs)) {
            urls = imgs.stream()
                    .map(Img::getUrl)
                    .distinct()
                    .collect(Collectors.toList());
        }
        return urls;
    }
}
