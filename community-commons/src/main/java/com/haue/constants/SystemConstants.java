package com.haue.constants;

public class SystemConstants {

    /**
     * 默认状态
     */
    public static final String DEFAULT_STATUS="-1";
    /**
     * 文章是正常发布状态
     */
    public static final String ARTICLE_STATUS_NORMAL="0";
    /**
     * 文章是草稿
     */
    public static final String ARTICLE_STATUS_DRAFT="1";
    /**
     * 文章正在审核中
     */
    public static final String ARTICLE_STATUS_REVIEW="2";
    /**
     * 文章未通过审核
     */
    public static final String ARTICLE_STATUS_REVIEW_FAILED="3";
    /**
     * 定时发布文章
     */
    public static final String ARTICLE_STATUS_SCHEDULED="4";
    /**
     * 分类是正常显示
     */
    public static final String STATUS_NORMAL="0";

    public static final String LINK_STATUS_NORMAL="0";
    /**
     * 审核记录-文章类型
     */
    public static final int CHECK_ARTICLE=0;
    /**
     * 审核记录-评论类型
     */
    public static final int CHECK_COMMENT=1;
    /**
     * 审核记录-动态类型
     */
    public static final int ACTIVITY_COMMENT=2;
    /**
     * 浏览量
     */
    public static final String ARTICLE_VIEWCOUNT="article:viewCount";
    /**
     * MenuType类型
     */
    public static final String MENU="C";
    /**
     * MenuType类型
     */
    public static final String BUTTON="F";
    public static final String ADMAIN = "1";
    public static final Long ADMAIN_ID = 14787164048669L;
    public static final String REDIS_LOGIN_KEY = "frontLogin:";
    public static final String REDIS_ADMIN_LOGIN_KEY = "adminLogin:";

    /**
     * 获取数据的默认页码
     */
    public static final int DEFAULT_PAGE_NUM = 1;
    /**
     * myCommunityList获取数据的条数
     */
    public static final int MY_COMMUNITY_LIST_PAGE_SIZE = 6;
    /**
     * 动态内容是转发文章内容
     */
    public static final String IS_REF_ARTICLE="1";
    /**
     * 动态内容是转发动态内容
     */
    public static final String IS_REF_ACTIVITY="2";
    /**
     * 获取当前用户的关注用户列表的默认条数
     */
    public static final int DEFAULT_PAGE_SIZE = 32;
    /**
     * 获取当前用户主页文章的默认条数
     */
    public static final int DEFAULT_ARTICLE_PAGE_SIZE = 10;
    /**
     * 获取当前用户主页文章的默认条数
     */
    public static final int DEFAULT_COMMUNITY_PAGE_SIZE = 6;
    /**
     * 默认用户id
     */
    public static final Long DEFAULT_USER_ID = -1L;
    /**
     * 默认社区id
     */
    public static final Long DEFAULT_COMMUNITY_ID = 0L;
    /**
     * 举报记录默认标志
     */
    public static final int DEFAULT_REPORT_FLAG_ID = 2;
    public static final String REQUEST_SUCCESS = "成功";
    /**
     * 审核通过
     */
    public static final String CHECK_SUCCESS = "pass";
    /**
     * 审核通过码
     */
    public static final int CHECK_SUCCESS_CODE = 0;
    /**
     * 审核不通过
     */
    public static final String CHECK_FAIL = "block";
    /**
     * 审核不同过码
     */
    public static final int CHECK_FAIL_CODE = 2;
    /**
     * 疑似有问题
     */
    public static final String CHECK_REVIEW = "review";
    /**
     * 问题码
     */
    public static final int CHECK_REVIEW_CODE = 1;
}
