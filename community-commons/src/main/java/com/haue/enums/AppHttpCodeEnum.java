package com.haue.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
     PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    CONTENT_NOT_NULL(506, "评论内容不能为空"),
    FILE_TYPE_ERROR(507, "文件类型错误，请上传png或jpg文件"),
    USERNAME_NOT_NULL(508, "用户名不能为空"),
    NICKNAME_NOT_NULL(509, "昵称不能为空"),
    PASSWORD_NOT_NULL(510, "密码不能为空"),
    EMAIL_NOT_NULL(511, "邮箱不能为空"),
    NICKNAME_EXIST(512, "昵称已存在"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    TAGNAME_EXIST(513,"标签已存在"),
    TAGID_NOT_EXIST(514,"标签不存在"),
    MENU_ERROR(515,"修改菜单失败，上级菜单不能选择自己或自己的子菜单"),
    MENU_CHILDREN_EXIST(516,"存在子菜单不允许删除"),
    ERROR_DELETE(517,"不能删除当前操作的用户"),
    INFO_NOT_NULL(518,"账号信息不能有空"),
    PARAMS_NOT_NULL(519,"参数不能有空"),
    TOKEN_NOT_EXIT(520,"token不能为空"),
    USERNAME_NOT_EXIST(521,"用户名不存在"),

    USERNAME_DISABLE(522,"用户名已被禁用"),
    CHECK_FAIL(600,"当前审核人数较多，请稍后再尝试上传"),
    REPUTATION_LOW(601,"您的信誉值过低，已被限制发布内容！请规范自己的言行！");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
