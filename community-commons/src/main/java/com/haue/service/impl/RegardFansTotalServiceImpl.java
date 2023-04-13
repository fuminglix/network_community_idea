package com.haue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.mapper.RegardFansTotalMapper;
import com.haue.pojo.entity.RegardFansTotal;
import com.haue.service.RegardFansTotalService;
import org.springframework.stereotype.Service;

/**
 * 用户关注信息表(RegardFansTotal)表服务实现类
 *
 * @author makejava
 * @since 2023-04-13 14:40:46
 */
@Service("regardFansTotalService")
public class RegardFansTotalServiceImpl extends ServiceImpl<RegardFansTotalMapper, RegardFansTotal> implements RegardFansTotalService {

}
