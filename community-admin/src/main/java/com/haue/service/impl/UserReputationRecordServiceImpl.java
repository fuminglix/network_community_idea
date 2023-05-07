package com.haue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.mapper.UserReputationRecordMapper;
import com.haue.pojo.entity.UserReputationRecord;
import com.haue.service.UserReputationRecordService;
import org.springframework.stereotype.Service;

/**
 * (UserReputationRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-05-07 14:16:48
 */
@Service("userReputationRecordService")
public class UserReputationRecordServiceImpl extends ServiceImpl<UserReputationRecordMapper, UserReputationRecord> implements UserReputationRecordService {

}
