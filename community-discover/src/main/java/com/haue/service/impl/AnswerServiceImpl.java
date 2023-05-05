package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.constants.SystemConstants;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.mapper.AnswerMapper;
import com.haue.pojo.entity.Answer;
import com.haue.pojo.param.AnswerParam;
import com.haue.pojo.vo.AnswerListVo;
import com.haue.pojo.vo.AnswerVo;
import com.haue.pojo.vo.AuthorInfoVo;
import com.haue.pojo.vo.PageVo;
import com.haue.service.AnswerService;
import com.haue.service.QuestionService;
import com.haue.service.UserService;
import com.haue.service.UserTotalService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (Answer)表服务实现类
 *
 * @author makejava
 * @since 2023-05-04 19:42:09
 */
@Service("answerService")
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTotalService userTotalService;

    /**
     * 获取回答列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult getAnswerList(Integer pageNum, Integer pageSize,Integer type) {
        LambdaQueryWrapper<Answer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Answer::getDelFlag, SystemConstants.STATUS_NORMAL)
                .eq(Answer::getStatus,SystemConstants.STATUS_NORMAL)
                .orderByDesc(Answer::getCreateTime);
        Page<Answer> page = new Page<>(pageNum, pageSize);
        page(page,wrapper);
        List<AnswerListVo> answerListVos = BeanCopyUtils.copyBeanList(page.getRecords(), AnswerListVo.class);
        return ResponseResult.okResult(new PageVo(answerListVos,page.getTotal()));
    }

    /**
     * 添加回答
     * @param param
     * @return
     */
    @Override
    @Transactional
    public ResponseResult addAnswer(AnswerParam param) {
        Answer answer = BeanCopyUtils.copyBean(param, Answer.class);
        boolean b = save(answer);
        if (!b){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    /**
     * 获取回答详情
     * @param id
     * @return
     */
    @Override
    public ResponseResult getAnswer(Long id) {
        Answer answer = getById(id);
        AnswerVo answerVo = BeanCopyUtils.copyBean(answer, AnswerVo.class);
        answerVo.setQuestionContent(questionService.getById(answerVo.getQuestionId()).getContent());
        answerVo.setUser(BeanCopyUtils.copyBean(userService.getById(answerVo.getCreateBy()), AuthorInfoVo.class)
                .setUserTotal(userTotalService.getById(answerVo.getCreateBy())));
        return ResponseResult.okResult(answerVo);
    }
}
