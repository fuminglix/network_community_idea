package com.haue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.mapper.QuestionAnswerMapper;
import com.haue.pojo.entity.QuestionAnswer;
import com.haue.service.QuestionAnswerService;
import org.springframework.stereotype.Service;

/**
 * (QuestionAnswer)表服务实现类
 *
 * @author makejava
 * @since 2023-05-04 19:42:53
 */
@Service("questionAnswerService")
public class QuestionAnswerServiceImpl extends ServiceImpl<QuestionAnswerMapper, QuestionAnswer> implements QuestionAnswerService {

}
