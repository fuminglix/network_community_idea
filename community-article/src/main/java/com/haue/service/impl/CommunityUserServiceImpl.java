package com.haue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.mapper.CommunityUserMapper;
import com.haue.pojo.entity.CommunityUser;
import com.haue.service.CommunityUserService;
import org.springframework.stereotype.Service;

/**
 * (CommunityUser)表服务实现类
 *
 * @author makejava
 * @since 2023-04-06 14:19:25
 */
@Service("communityUserService")
public class CommunityUserServiceImpl extends ServiceImpl<CommunityUserMapper, CommunityUser> implements CommunityUserService {

}
