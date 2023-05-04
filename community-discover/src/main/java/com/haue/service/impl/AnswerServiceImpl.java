package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.constants.SystemConstants;
import com.haue.mapper.AnswerMapper;
import com.haue.pojo.entity.Answer;
import com.haue.pojo.vo.AnswerListVo;
import com.haue.pojo.vo.PageVo;
import com.haue.service.AnswerService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Answer)表服务实现类
 *
 * @author makejava
 * @since 2023-05-04 19:42:09
 */
@Service("answerService")
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {



    /**
     * 获取回答列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult getAnswerList(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Answer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Answer::getDelFlag, SystemConstants.STATUS_NORMAL)
                .eq(Answer::getStatus,SystemConstants.STATUS_NORMAL)
                .orderByDesc(Answer::getCreateTime);
        Page<Answer> page = new Page<>(pageNum, pageSize);
        page(page,wrapper);
        List<AnswerListVo> answerListVos = BeanCopyUtils.copyBeanList(page.getRecords(), AnswerListVo.class);
        return ResponseResult.okResult(new PageVo(answerListVos,page.getTotal()));
    }
}
