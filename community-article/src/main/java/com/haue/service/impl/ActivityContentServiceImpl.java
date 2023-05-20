package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.constants.SystemConstants;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.mapper.ActivityContentMapper;
import com.haue.pojo.entity.ActivityContent;
import com.haue.pojo.entity.Img;
import com.haue.pojo.entity.RegardFansTotal;
import com.haue.pojo.params.AddActivityContentParam;
import com.haue.pojo.vo.ActivityContentVo;
import com.haue.pojo.vo.AuthorInfoVo;
import com.haue.pojo.vo.PageVo;
import com.haue.pojo.vo.RecommendArticleVo;
import com.haue.service.*;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import com.haue.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (ActivityContent)表服务实现类
 *
 * @author makejava
 * @since 2023-04-12 10:42:57
 */
@Service("activityContentService")
public class ActivityContentServiceImpl extends ServiceImpl<ActivityContentMapper, ActivityContent> implements ActivityContentService {


    @Autowired
    private RegardFansTotalService regardFansTotalService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImgService imgService;


    /**
     * 查询当前用户的动态信息
     *
     * @param pageNum
     * @param pageSize
     * @param id
     * @return
     */
    @Override
    public ResponseResult getActivityInfo(Integer pageNum,Integer pageSize, Long id) {
        Page<ActivityContent> page = new Page<>(pageNum, pageSize);
        if (SystemConstants.DEFAULT_USER_ID.equals(id)){ //判断是否需要查询全部动态
            //获取当前用户id
            Long userId = SecurityUtils.getUserId();
            //查询当前用户的关注列表
            LambdaQueryWrapper<RegardFansTotal> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(RegardFansTotal::getId,userId);
            List<Long> list = regardFansTotalService.list(wrapper).stream()
                    .map(RegardFansTotal::getRegardId)
                    .collect(Collectors.toList());
            list.add(userId);

            //查询当前用户和其关注用户的动态
            LambdaQueryWrapper<ActivityContent> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(ActivityContent::getCreateBy,list)
                    .eq(ActivityContent::getCommunityId,SystemConstants.DEFAULT_COMMUNITY_ID)
                    .orderByDesc(ActivityContent::getCreateTime);
            page(page,queryWrapper);
            //处理动态中的图片和部分转发动态
            List<ActivityContentVo> contentVos = toActivityContentVo(page.getRecords());
            return ResponseResult.okResult(new PageVo(contentVos,page.getTotal()));
        }
        //查询某个当前用户关注用户的动态
        LambdaQueryWrapper<ActivityContent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ActivityContent::getCreateBy,id)
                .eq(ActivityContent::getCommunityId,SystemConstants.DEFAULT_COMMUNITY_ID)
                .orderByDesc(ActivityContent::getCreateTime);
        page(page,queryWrapper);
        //处理动态中的图片和部分转发动态
        List<ActivityContentVo> contentVos = toActivityContentVo(page.getRecords());
        return ResponseResult.okResult(new PageVo(contentVos,page.getTotal()));
    }

    /**
     * 添加动态
     * @param activityContentParam
     * @return
     */
    @Override
    @Transactional
    public ResponseResult addActivityContent(AddActivityContentParam activityContentParam) {
        Integer reputation = userService.getById(SecurityUtils.getUserId()).getReputation();
        if (reputation <= SystemConstants.DEFAULT_REPUTATION){
            return ResponseResult.errorResult(AppHttpCodeEnum.REPUTATION_LOW);
        }
        ActivityContent activityContent = BeanCopyUtils.copyBean(activityContentParam, ActivityContent.class);
        save(activityContent);

        if (activityContentParam.getImgList().size() > 0){
            List<Img> imgs = activityContentParam.getImgList();
            List<Img> imgList = BeanCopyUtils.copyBeanList(imgs, Img.class).stream()
                    .peek(img -> img.setActivityId(activityContent.getId()))
                    .distinct()
                    .collect(Collectors.toList());
            imgService.saveBatch(imgList);
        }
        return ResponseResult.okResult();
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
//                .map(content -> SystemConstants.IS_REF.equals(content.getIsRef()) ? //判断是不是转发内容
//                        content.setArticleVo //将转发的文章信息封装到ArticleVo
//                                (BeanCopyUtils.copyBean(articleService.getById(content.getRefId()), RecommendArticleVo.class).setUser //将转发文章的作者信息封装到ArticleVo中
//                                        (BeanCopyUtils.copyBean(userService.getById(articleService.getById(content.getRefId()).getCreateBy()), AuthorInfoVo.class))) : content)
//                .map(content -> content.setUser(BeanCopyUtils.copyBean(userService.getById(content.getCreateBy()), AuthorInfoVo.class))) //将动态的作者信息封装到ActivityContentVo中
//                .collect(Collectors.toList());

        //将动态的图片封装到ActivityContentVo中
//        for (ActivityContentVo content : contents) {
//            List<Img> list = imgService.list(new LambdaQueryWrapper<Img>().eq(Img::getActivityId, content.getId()));
//            if (Objects.nonNull(list)) {
//                List<String> urls = list.stream()
//                        .map(Img::getUrl)
//                        .distinct()
//                        .collect(Collectors.toList());
//                content.setContentImg(urls);
//            }
//        }
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
