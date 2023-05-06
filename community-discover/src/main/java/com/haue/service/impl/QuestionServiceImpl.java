package com.haue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.constants.SystemConstants;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.exception.SystemException;
import com.haue.mapper.QuestionMapper;
import com.haue.pojo.entity.Question;
import com.haue.pojo.param.QuestionParam;
import com.haue.pojo.vo.AuthorInfoVo;
import com.haue.pojo.vo.PageVo;
import com.haue.pojo.vo.QuestionListVo;
import com.haue.pojo.vo.QuestionVo;
import com.haue.service.QuestionService;
import com.haue.service.UserService;
import com.haue.utils.BeanCopyUtils;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * (Question)表服务实现类
 *
 * @author makejava
 * @since 2023-05-04 19:42:44
 */
@Service("questionService")
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Autowired
    private UserService userService;

    /**
     * 添加问题
     * @param param
     * @return
     */
    @Override
    @Transactional
    public ResponseResult addQuestion(QuestionParam param) {
        Question question = BeanCopyUtils.copyBean(param, Question.class);
        boolean b = save(question);
        if (!b){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    /**
     * 获取问题列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult getQuestionList(Integer pageNum, Integer pageSize,Integer type,String sort) {
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getDelFlag, SystemConstants.STATUS_NORMAL)
                .eq(Question::getType,type)
                .orderByDesc(StringUtils.hasText(sort),Question::getAnswerCount)
                .orderByDesc(!StringUtils.hasText(sort),Question::getCreateTime);
        Page<Question> page = new Page<>(pageNum, pageSize);
        page(page,wrapper);
        List<QuestionListVo> listVos = BeanCopyUtils.copyBeanList(page.getRecords(), QuestionListVo.class);
        listVos.forEach( question -> {
            question.setUser(BeanCopyUtils.copyBean(userService.getById(question.getCreateBy()), AuthorInfoVo.class));
        });
        return ResponseResult.okResult(new PageVo(listVos,page.getTotal()));
    }

    /**
     * 获取问题详情
     * @param id
     * @return
     */
    @Override
    public ResponseResult getQuestion(Long id) {
        Question question = getById(id);
        if (!Objects.nonNull(question)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        QuestionVo questionVo = BeanCopyUtils.copyBean(question, QuestionVo.class);
        return ResponseResult.okResult(questionVo);
    }
}
