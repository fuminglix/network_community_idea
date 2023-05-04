package com.haue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haue.pojo.entity.Question;
import org.springframework.stereotype.Repository;

/**
 * (Question)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-04 19:42:44
 */
@Repository
public interface QuestionMapper extends BaseMapper<Question> {

}
