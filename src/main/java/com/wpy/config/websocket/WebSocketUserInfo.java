package com.wpy.config.websocket;

import lombok.Data;

import javax.websocket.Session;

/**
 * @author 13940
 * @date 2021-4-2
 */

@Data
public class WebSocketUserInfo {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户账号
     */
    private String accountName;

    /**
     * 用户名
     */
    private String username;

    /**
     * 小说id
     */
    private String novelId;

    /**
     * 章节id
     */
    private String chapterId;

    /**
     * session
     */
    private Session session;
}
