package com.haue.pojo.params;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.haue.pojo.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleParam {
    private Long id;
    //标题
    @NotBlank(message = "title不能为空")
    private String title;
    //文章摘要
    @NotBlank(message = "summary不能为空")
    private String summary;
    //文章内容
    @NotBlank(message = "content不能为空")
    private String content;
    //所属分类id
    @NotNull(message = "categoryId不能为空")
    private Long categoryId;
    //所属社区id
    @NotNull(message = "communityId不能为空")
    private Long communityId;
    //标签
    private List<Tag> tags;
    //缩略图
    private String thumbnail;
    //是否置顶（0否，1是）
    private String isTop;
    //状态（0已发布，1草稿 2 审核中 3 未通过 4 定时发布）
    @NotBlank(message = "status不能为空")
    private String status;
    //是否允许评论 1是，0否
    @NotBlank(message = "isComment不能为空")
    private String isComment;
    //文章类型（0 原创 1 转载）
    @NotBlank(message = "articleType不能为空")
    private String articleType;
    //发布形式（0 全部可见 1 仅我可见 2 粉丝可见）
    @NotBlank(message = "releaseForm不能为空")
    private String releaseForm;
    //定时发布时间
    private Date scheduled;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
}
