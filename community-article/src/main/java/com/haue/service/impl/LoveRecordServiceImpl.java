package com.haue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.mapper.LoveRecordMapper;
import com.haue.pojo.entity.LoveRecord;
import com.haue.service.LoveRecordService;
import org.springframework.stereotype.Service;

/**
 * (LoveRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-05-09 17:41:35
 */
@Service("loveRecordService")
public class LoveRecordServiceImpl extends ServiceImpl<LoveRecordMapper, LoveRecord> implements LoveRecordService {

}
