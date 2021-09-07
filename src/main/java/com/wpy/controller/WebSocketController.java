package com.wpy.controller;

import com.wpy.service.WebSocketService;
import com.wpy.utils.SpringUtils;
import com.wpy.config.websocket.WebSocketUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author 13940
 */

@Component
@DependsOn("springUtils")
@ServerEndpoint( value = "/novel/chat/{accountName}/{novelId}/{chapterId}" )
public class WebSocketController {

    /**
     * webSocketService
     */
    private static final WebSocketService webSocketService =SpringUtils.getBean(WebSocketService.class);

    /**
     * 连接事件，加入注解
     * @param accountName
     * @param novelId
     * @param chapterId
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam( value = "accountName" ) String accountName,
                       @PathParam(value = "novelId")String novelId,
                       @PathParam(value = "chapterId")String chapterId,
                       Session session ) {
        //添加用户
        webSocketService.addUser(accountName,novelId,chapterId,session);

    }

    /**
     * 连接事件，加入注解
     * 用户断开链接
     * @param accountName
     * @param session
     */
    @OnClose
    public void onClose(@PathParam( value = "accountName" ) String accountName,
                        @PathParam(value = "novelId")String novelId,
                        @PathParam(value = "chapterId")String chapterId,
                        Session session ) {
        webSocketService.exitChat(accountName,novelId,chapterId,session);
    }

    /**
     * 当接收到用户上传的消息
     * @param accountName
     * @param session
     */
    @OnMessage
    public void onMessage(@PathParam( value = "accountName" ) String accountName,
                          @PathParam(value = "novelId")String novelId,
                          @PathParam(value = "chapterId")String chapterId,
                          Session session ,
                          String message) {
        webSocketService.sendMessage(accountName,novelId,chapterId,session,message);
    }

    /**
     * 处理用户活连接异常
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throwable.printStackTrace();
    }
}
