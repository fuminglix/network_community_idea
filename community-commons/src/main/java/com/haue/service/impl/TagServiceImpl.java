package com.haue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.mapper.TagMapper;
import com.haue.pojo.entity.Tag;
import com.haue.service.TagService;
import org.springframework.stereotype.Service;

/**
 * (Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-04-01 16:13:01
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}
