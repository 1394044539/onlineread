package com.wpy.service;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.websocket.Session;

/**
 * websocket服务层
 * @author 13940
 */
public interface WebSocketService {

    /**
     * 添加用户
     * @param accountName
     * @param novelId
     * @param chapterId
     * @param session
     */
    void addUser(String accountName, String novelId, String chapterId, Session session);

    /**
     * 退出聊天室
     * @param accountName
     * @param novelId
     * @param chapterId
     * @param session
     */
    void exitChat(String accountName, String novelId, String chapterId, Session session);

    /**
     * 发送消息
     * @param accountName
     * @param novelId
     * @param chapterId
     * @param session
     * @param message
     */
    void sendMessage(String accountName, String novelId, String chapterId, Session session, String message);
}
