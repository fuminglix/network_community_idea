package com.haue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haue.pojo.entity.Answer;
import com.haue.utils.ResponseResult;


/**
 * (Answer)表服务接口
 *
 * @author makejava
 * @since 2023-05-04 19:42:09
 */
public interface AnswerService extends IService<Answer> {

    ResponseResult getAnswerList(Integer pageNum, Integer pageSize);
}
