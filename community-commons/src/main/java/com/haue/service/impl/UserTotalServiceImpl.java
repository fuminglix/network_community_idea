package com.haue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.mapper.UserTotalMapper;
import com.haue.pojo.entity.UserTotal;
import com.haue.service.UserTotalService;
import org.springframework.stereotype.Service;

/**
 * 用户数据统计信息表(UserTotal)表服务实现类
 *
 * @author makejava
 * @since 2023-04-01 19:15:37
 */
@Service("userTotalService")
public class UserTotalServiceImpl extends ServiceImpl<UserTotalMapper, UserTotal> implements UserTotalService {

}
