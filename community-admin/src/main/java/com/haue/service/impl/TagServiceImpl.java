package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.exception.SystemException;
import com.haue.mapper.TagMapper;
import com.haue.params.TagListParam;
import com.haue.pojo.entity.Tag;
import com.haue.pojo.vo.PageVo;
import com.haue.service.TagService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import com.haue.vo.TagVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-04-18 18:22:02
 */
@Service("systemTagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    /**
     * 查询标签
     * @param pageNum
     * @param pageSize
     * @param tagListParam
     * @return
     */
    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListParam tagListParam) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(tagListParam.getName()),Tag::getTagName,tagListParam.getName());

        Page<Tag> page = new Page<>(pageNum, pageSize);
        page(page,wrapper);

        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 添加标签
     * @param tag
     * @return
     */
    @Override
    public ResponseResult addTag(Tag tag) {
        List<Tag> tagList = list();
        List<String> tags = tagList.stream()
                .map(Tag::getTagName)
                .distinct()
                .filter(name -> name.equals(tag.getTagName()))
                .collect(Collectors.toList());
        if (tags.size()>0){
            return ResponseResult.errorResult(AppHttpCodeEnum.TAGNAME_EXIST.getCode(),AppHttpCodeEnum.TAGNAME_EXIST.getMsg());
        }
        save(tag);
        return ResponseResult.okResult();
    }

    /**
     * 删除标签
     * @param id
     * @return
     */
    @Override
    public ResponseResult deleteTag(Long id) {
        if (id == null || getById(id) == null){
            throw new SystemException(AppHttpCodeEnum.TAGID_NOT_EXIST);
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 修改标签
     * @param id
     * @return
     */
    @Override
    public ResponseResult showUpdateInfo(Long id) {
        if (id == null){
            throw new SystemException(AppHttpCodeEnum.TAGID_NOT_EXIST);
        }
        return ResponseResult.okResult(BeanCopyUtils.copyBean(getById(id), TagVo.class));
    }

    @Override
    public ResponseResult updateTag(Tag tag) {
//        判断id是否为空
        if (tag.getId() == null ||  getById(tag.getId()) == null){
            throw new SystemException(AppHttpCodeEnum.TAGID_NOT_EXIST);
        }
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getTagName,tag.getTagName());

        List<Tag> listTag = list(wrapper);
        if (listTag.size()>0 && !listTag.get(0).getId().equals(tag.getId())){
            throw new SystemException(AppHttpCodeEnum.TAGNAME_EXIST);
        }
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> list = list(wrapper);
        return ResponseResult.okResult(BeanCopyUtils.copyBeanList(list, TagVo.class));
    }
}
