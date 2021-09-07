package com.wpy.enums;

/**
 * 错误码枚举
 * @author 13940
 */
public enum CodeMsgEnums {

    SUCCESS(200,"操作成功"),
    FAIL(0,"操作失败"),


    USER_NOT_LOGIN(-10000,"用户未登录"),
    ID_IS_EMPTY(-1,"id不能为空"),
    USER_ID_IS_EMPTY(-1,"用户id不能为空"),
    NOVEL_NOT_EXIST(-2,"小说不存在"),
    PARAM_ERROR(-3,"参数错误"),
    OLD_PWD_ERROE(-100,"原密码错误"),
    VERIFY_CODE_ERROR(-100,"验证码错误"),
    TITLE_NOT_EMPTY(-4,"标题不能为空"),
    CONTENT_NOT_EMPTY(-5,"内容不能为空"),
    NOT_NOVEL_PERMISSION(-6,"无小说权限"),
    NOVEL_IS_DISABLE(-7,"该小说已被禁用"),
    NOVEL_STATUS_ERROE(-8,"审核状态错误");

    private Integer code;
    private String msg;

    private CodeMsgEnums(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    private CodeMsgEnums(String msg){
        this.msg=msg;
    }

    public String getMsg(){
        return msg;
    }

    private CodeMsgEnums(int code, String msg){
        this.code=code;
        this.msg=msg;
    }
}
