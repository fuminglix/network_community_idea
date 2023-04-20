package com.haue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haue.mapper.ImgMapper;
import com.haue.pojo.entity.Img;
import com.haue.service.ImgService;
import org.springframework.stereotype.Service;

/**
 * (Img)表服务实现类
 *
 * @author makejava
 * @since 2023-04-20 20:26:45
 */
@Service("systemImgService")
public class ImgServiceImpl extends ServiceImpl<ImgMapper, Img> implements ImgService {

}
