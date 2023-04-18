package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.params.TagListParam;
import com.haue.pojo.entity.Tag;
import com.haue.pojo.vo.PageVo;
import com.haue.utils.ResponseResult;


/**
 * (Tag)表服务接口
 *
 * @author makejava
 * @since 2023-04-18 18:22:02
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListParam tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult deleteTag(Long id);

    ResponseResult showUpdateInfo(Long id);

    ResponseResult updateTag(Tag tag);

    ResponseResult listAllTag();
}
