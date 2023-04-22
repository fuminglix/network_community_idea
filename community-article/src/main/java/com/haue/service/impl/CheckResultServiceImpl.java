package com.haue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.mapper.CheckResultMapper;
import com.haue.pojo.entity.CheckResult;
import com.haue.service.CheckResultService;
import org.springframework.stereotype.Service;

/**
 * (CheckResult)表服务实现类
 *
 * @author makejava
 * @since 2023-04-22 16:42:05
 */
@Service("checkResultService")
public class CheckResultServiceImpl extends ServiceImpl<CheckResultMapper, CheckResult> implements CheckResultService {

}
