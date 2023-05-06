package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.pojo.entity.Answer;
import com.haue.pojo.param.AnswerParam;
import com.haue.utils.ResponseResult;


/**
 * (Answer)表服务接口
 *
 * @author makejava
 * @since 2023-05-04 19:42:09
 */
public interface AnswerService extends IService<Answer> {

    ResponseResult getAnswerListByType(Integer pageNum, Integer pageSize, Integer type);

    ResponseResult addAnswer(AnswerParam param);

    ResponseResult getAnswer(Long id);

    ResponseResult getAnswerListById(Integer pageNum, Integer pageSize, Long questionId);
}
