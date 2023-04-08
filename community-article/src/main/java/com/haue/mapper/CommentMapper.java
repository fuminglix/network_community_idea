package com.haue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haue.pojo.entity.Comment;
import org.springframework.stereotype.Repository;

/**
 * 评论表(Comment)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-02 16:58:27
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

}
