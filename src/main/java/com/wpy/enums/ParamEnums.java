package com.wpy.enums;

/**
 * 参数枚举
 * @author pywang
 * @date 2020/1/6
 */
public enum ParamEnums {

    PHONE_LOG_OR_REG(0,"手机号登录注册"),
    ACCOUNT_LOG_OR_REG(1,"账号登录注册"),
    EMAIL_LOG_OR_REG(2,"邮箱登录注册"),

    TXT(0,"txt"),
    EPUB(1,"epub"),


    IS_DELETE(1,"已删除"),
    NOT_DELETE(0,"未删除"),

    ORDER_ASC(0,"asc"),
    ORDER_DESC(1,"desc")
    ;

    private Integer code;
    private String msg;

    private ParamEnums(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    private ParamEnums(String msg){
        this.msg=msg;
    }

    public String getMsg(){
        return msg;
    }

    private ParamEnums(int code, String msg){
        this.code=code;
        this.msg=msg;
    }
}
