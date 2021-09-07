package com.wpy.config.websocket;

import com.wpy.entity.Chat;
import com.wpy.utils.JsonUtils;
import com.wpy.config.websocket.WebSocketUserInfo;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 工具类
 * @author 13940
 *
 */
@Slf4j
public class WebSocketUtils {

    /**
     * 记录当前在线的Session
     */
    private static final Map<String, WebSocketUserInfo> ONLINE_SESSION = new ConcurrentHashMap<>();

    /**
     * 添加session
     * @param accountName
     * @param webSocketUserInfo
     */
    public static void addSession(String accountName, WebSocketUserInfo webSocketUserInfo){
        //
        ONLINE_SESSION.put ( accountName, webSocketUserInfo );
    }

    /**
     * 关闭session
     * @param accountName
     */
    public static void removeSession(String accountName){
        ONLINE_SESSION.remove ( accountName );
    }

    /**
     * 给单个用户推送消息
     * @param session
     * @param message
     */
    public static void sendMessage(Session session, String message){
        if(session == null){
            return;
        }
        // 同步
        RemoteEndpoint.Async async  = session.getAsyncRemote ();
        async.sendText ( message );
    }

    public static void sendMessage(Session session, WebSocketPojo webSocketPojo){
        if(session == null){
            return;
        }
        String message = JsonUtils.ObjectToJsonStr(webSocketPojo);
        log.info("发送消息：{}",message);
        synchronized(session) {
            // 同步
            RemoteEndpoint.Basic basicRemote = session.getBasicRemote();
            try {
                basicRemote.sendText ( message );
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

    }

    /**
     * 向所有在线人发送消息
     * @param message
     */
    public static void sendMessageForAll(String message) {
        //jdk8 新方法
        ONLINE_SESSION.forEach((sessionId, info) -> sendMessage(info.getSession(), message));
    }

    /**
     * 获取当前在线用户
     * @return
     */
    public static Map<String,WebSocketUserInfo> getOnlineSession(){
        return ONLINE_SESSION;
    }

    public static WebSocketUserInfo getUserInfo(String accountName){
        return ONLINE_SESSION.get(accountName);
    }
}
