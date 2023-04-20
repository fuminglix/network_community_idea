package com.haue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.mapper.ActivityContentMapper;
import com.haue.pojo.entity.ActivityContent;
import com.haue.service.ActivityContentService;
import org.springframework.stereotype.Service;

/**
 * (ActivityContent)表服务实现类
 *
 * @author makejava
 * @since 2023-04-20 19:44:33
 */
@Service("systemActivityContentService")
public class ActivityContentServiceImpl extends ServiceImpl<ActivityContentMapper, ActivityContent> implements ActivityContentService {

}
