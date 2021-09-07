package com.wpy.config.websocket;

/**
 * @author 13940
 * @date 2021/4/3
 */
public enum MessageType {

    NOTICT_MSG(0,"通知消息"),
    CHAT_MSG(1,"聊天消息"),
    USER_MSG(2,"用户自己"),
    ONLINE_NUM(3,"在线人数");

    private Integer code;
    private String msg;

    private MessageType(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    private MessageType(String msg){
        this.msg=msg;
    }

    public String getMsg(){
        return msg;
    }

    private MessageType(int code, String msg){
        this.code=code;
        this.msg=msg;
    }
}
