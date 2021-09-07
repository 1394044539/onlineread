package com.wpy.enums;

/**
 * 字典枚举
 * @author pywang6
 * @date 2021/3/12 10:22
 */
public enum DictEnums {

    NOVEL_TYPE("NOVEL_TYPE","小说类型"),
    ADMIN_CHOSE_TYPE("ADMIN_CHOSE_TYPE","首页选择的分类"),

    ADMIN_INIT_PWD("ADMIN_INIT_PWD","管理员初始密码"),
    ;

    private String key;
    private String msg;

    public String getKey() {
        return key;
    }

    private DictEnums(String key){
        this.key=key;
        this.msg="暂无描述";
    }

    public String getMsg(){
        return msg;
    }

    private DictEnums(String key, String msg){
        this.key=key;
        this.msg=msg;
    }
}
