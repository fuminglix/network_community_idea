package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.pojo.entity.Question;
import com.haue.pojo.param.QuestionParam;
import com.haue.utils.ResponseResult;


/**
 * (Question)表服务接口
 *
 * @author makejava
 * @since 2023-05-04 19:42:44
 */
public interface QuestionService extends IService<Question> {

    ResponseResult addQuestion(QuestionParam param);

    ResponseResult getQuestionList(Integer pageNum, Integer pageSize,Integer type,String sort);

    ResponseResult getQuestion(Long id);
}
