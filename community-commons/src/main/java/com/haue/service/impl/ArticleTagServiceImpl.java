package com.haue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.mapper.ArticleTagMapper;
import com.haue.pojo.entity.ArticleTag;
import com.haue.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * (ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-04-01 16:16:39
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
