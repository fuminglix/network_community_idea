package com.haue.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.haue.pojo.entity.Img;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ActivityContentVo {

    //动态id
    private Long id;

    //动态内容
    private String content;
    //转发标志（0代表非转发 1转发）
    private String isRef;
    //转发的文章id
    private Long refId;
    //转发时添加的内容
    private String dispatchContent;
    //创建用户id
    private Long createBy;
    //创建时间
    private Date createTime;
    //社区id
    private Long communityId;
    //转发数
    private Long relayCount;
    //评论数
    private Long commentCount;
    //点赞数
    private Long loveCount;
    //动态图片
    private List<String> contentImg;
    //转发的文章信息
    private RecommendArticleVo articleVo;
    //动态的作者信息
    private AuthorInfoVo user;
}
