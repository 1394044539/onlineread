package com.wpy.config.websocket;

import lombok.Data;

import java.util.Date;

/**
 * @author 13940
 * @date 2021/4/2
 */
@Data
public class WebSocketPojo {

    /**
     * 用户账号
     */
    private String accountName;

    /**
     * 用户名
     */
    private String username;
    /**
     * 消息类型:0:通知消息；1：用户消息
     */
    private Integer msgType;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 消息时间
     */
    private Date messageTime;
}
