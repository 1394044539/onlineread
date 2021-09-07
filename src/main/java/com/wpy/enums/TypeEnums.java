package com.wpy.enums;

public enum TypeEnums {

    ADMIN_ROLE(0,"管理员"),
    ORDINARY_USER(1,"普通用户"),

    PUBLIC_NOVEL(0,"公开"),
    PRIVATE_NOVEL(1,"私有"),

    //审核状态:0:未审核；1:待审核;2:审核完成;3：审核失败，4：被禁用
    NOT_AUDIT(0,"未审核"),
    WAIT_AUDIT(1,"待审核"),
    SUCCESS_AUDIT(2,"审核完成"),
    FAIL_AUDIT(3,"审核失败"),
    NOVEL_DISABLE(4,"被禁用"),

    MAIN_PAGE_TYPR(0,"首页类型"),
    PERSON_PAGE_TYPE(1,"个人页面类型"),
    ADMIN_PAGE_TYPE(2,"管理员页面类型"),

    HOT_SORT(0,"热度排序"),
    TIME_SORT(1,"时间排序"),

    PERSON_NOVEL(0,"个人"),
    ALL_NOVEL(1,"非个人"),

    USER_CHANNEL(0,"用户渠道"),
    SYS_CHANNEL(1,"平台渠道"),

    IS_COLLECTION(1,"已收藏"),
    NOT_COLLECTION(0,"未收藏"),

    NOT_LOGIN(1,"未登录"),
    IS_LOGIN(0,"已登录"),

    BOOK_MARK(1,"书签"),
    HISTORY_RECORD(0,"历史记录"),

    CHOSE_DELETE(0,"勾选删除"),
    PERSON_REMOVE(1,"用户清空"),
    ADMIN_CHOSE_DELETE(2,"管理员清空"),
    ADMIN_REMOVEL_ALL(3,"管理员清空全部"),

    FIND_PWD_BY_PHONE(0,"手机号找回密码"),
    FIND_PWD_BY_PWD(1,"密码修改"),

    CATALOG_TYPE(0,"文件夹"),
    NOVEL_TYPE(1,"小说"),
    ALL_TYPE(2,"全部"),

    MOVE_TYPE(0,"移动"),
    COPY_TYPE(1,"复制"),

    SHARE_EFFECTIVE(0,"有效"),
    SHARE_DELETE(1,"删除"),
    SHARE_DISABLE(2,"禁用"),

    //分享类型：0：随意进入;1:纯密码进入;2：指定用户分享;3:指定用户密码分享
    ANY_USER(0,"随意进入"),
    ONLY_PWD(1,"密码进入"),
    ASSING_USER(2,"指定用户"),
    ASSING_PWD_USER(3,"指定用户密码分享"),

    NOT_READ(0,"未阅读"),
    ALREADY_READ(1,"已阅读"),

    UPLOAD_DYNAMIC(0,"上传动态"),
    AUDIT_DYNAMIC(1,"审核动态"),

    //用户状态
    NORMAL_STATUS(0,"正常"),
    LOGOUT_STATUS(1,"注销"),
    DISABLE_STATUS(2,"禁用"),
    ;

    private Integer code;
    private String msg;

    private TypeEnums(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    private TypeEnums(String msg){
        this.msg=msg;
    }

    public String getMsg(){
        return msg;
    }

    private TypeEnums(int code, String msg){
        this.code=code;
        this.msg=msg;
    }
}
