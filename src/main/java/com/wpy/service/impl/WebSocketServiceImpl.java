package com.wpy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wpy.config.websocket.MessageType;
import com.wpy.config.websocket.WebSocketPojo;
import com.wpy.config.websocket.WebSocketUserInfo;
import com.wpy.config.websocket.WebSocketUtils;
import com.wpy.constant.SqlConstant;
import com.wpy.entity.Chat;
import com.wpy.entity.SysUser;
import com.wpy.mapper.ChatMapper;
import com.wpy.service.ChatService;
import com.wpy.service.SysUserService;
import com.wpy.service.WebSocketService;
import com.wpy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * websocket实现类
 * @author 13940
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private ChatService chatService;
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public void addUser(String accountName, String novelId, String chapterId, Session session) {
        //1、获取当前用户信息
        SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq(SqlConstant.ACCOUNT_NAME, accountName));
        //1、保存当前用户信息
        WebSocketUserInfo webSocketUserInfo=new WebSocketUserInfo();
        webSocketUserInfo.setUserId(sysUser.getId());
        webSocketUserInfo.setUsername(sysUser.getUserName());
        webSocketUserInfo.setAccountName(accountName);
        webSocketUserInfo.setNovelId(novelId);
        webSocketUserInfo.setChapterId(chapterId);
        webSocketUserInfo.setSession(session);
        WebSocketUtils.addSession(accountName,webSocketUserInfo);

        //2、给当前的用户从数据库捞出最新的10条聊天数据
        List<Chat> chats = this.chatMapper.selectList(new QueryWrapper<Chat>()
                .eq(SqlConstant.NOVEL_ID, novelId)
                .eq(SqlConstant.CHAPTER_ID, chapterId)
                .in(SqlConstant.MSG_TYPE,MessageType.NOTICT_MSG,MessageType.CHAT_MSG.getCode(),MessageType.USER_MSG.getCode())
                .orderByDesc(SqlConstant.SEND_TIME).last("limit 10"));
        //取出的数据再按照正序排
        if(!chats.isEmpty()){
            chats=chats.stream().sorted(Comparator.comparing(Chat::getSendTime)).collect(Collectors.toList());
        }
        for (Chat chat : chats) {
            if(chat.getMsgType().equals(MessageType.NOTICT_MSG.getCode())){
                this.sendMessageForNovelIdAndChapterId(chat.getNovelId(),chat.getChapterId(),chat.getMessage(),chat.getMsgType());
            }else if(chat.getSendUserId().equals(sysUser.getId())){
                this.sendMessageForNovelIdAndChapterId(chat.getNovelId(),chat.getChapterId(),chat.getMessage(),MessageType.USER_MSG.getCode());
            }else{
                this.sendMessageForNovelIdAndChapterId(chat.getNovelId(),chat.getChapterId(),chat.getMessage(),MessageType.CHAT_MSG.getCode());
            }
        }

        //3、通知当前章节的用户当前用户已经上线
        String userUpMsg=accountName+"用户已上线";
        this.sendMessageForNovelIdAndChapterId(novelId,chapterId,userUpMsg, MessageType.NOTICT_MSG.getCode());
        //保存上线提示信息
        Chat chat=new Chat(StringUtils.getUuid(),userUpMsg,sysUser.getId(),new Date(),novelId,chapterId,MessageType.NOTICT_MSG.getCode());
        this.chatService.save(chat);

        //4、当前在线人数+1
        this.sendMessageOnlineNum(novelId,chapterId);
    }

    @Override
    public void exitChat(String accountName, String novelId, String chapterId, Session session) {
        WebSocketUserInfo userInfo = WebSocketUtils.getUserInfo(accountName);
        //1、移除当前人的信息
        WebSocketUtils.removeSession(accountName);

        //2、通知当前章节用户已下线
        String exitMsg=accountName+"用户已下线";
        this.sendMessageForNovelIdAndChapterId(novelId,chapterId,exitMsg,MessageType.NOTICT_MSG.getCode());
        //保存下线提示信息
        Chat chat=new Chat(StringUtils.getUuid(),exitMsg,userInfo.getUserId(),new Date(),novelId,chapterId,MessageType.NOTICT_MSG.getCode());
        this.chatService.save(chat);

        //3、当前在线人数减一
        this.sendMessageOnlineNum(novelId,chapterId);
    }

    @Override
    public void sendMessage(String accountName, String novelId, String chapterId, Session session, String message) {
        //1、群发
        Map<String, WebSocketUserInfo> onlineSession = WebSocketUtils.getOnlineSession();
        WebSocketUserInfo sendUserInfo=WebSocketUtils.getUserInfo(accountName);
        for (Map.Entry<String, WebSocketUserInfo> entry : onlineSession.entrySet()) {
            WebSocketUserInfo userInfo=entry.getValue();
            //发送消息
            WebSocketPojo webSocketPojo=new WebSocketPojo();
            webSocketPojo.setAccountName(accountName);
            webSocketPojo.setUsername(userInfo.getUsername());
            webSocketPojo.setMessageTime(new Date());
            webSocketPojo.setMessage(message);
            if(accountName.equals(userInfo.getAccountName())){
                webSocketPojo.setMsgType(MessageType.USER_MSG.getCode());
            }else if(userInfo.getNovelId().equals(novelId)&&userInfo.getChapterId().equals(chapterId)){
                //发送消息
                webSocketPojo.setMsgType(MessageType.CHAT_MSG.getCode());
            }
            WebSocketUtils.sendMessage(userInfo.getSession(),webSocketPojo);
        }

        //2.保存到数据库
        Chat chat=new Chat(StringUtils.getUuid(),message,sendUserInfo.getUserId(),new Date(),novelId,chapterId,MessageType.CHAT_MSG.getCode());
        this.chatService.save(chat);
    }

    /**
     * 向指定在线人发送消息
     * @param message
     */
    private void sendMessageForNovelIdAndChapterId(String novelId,String chapterId,String message,Integer type) {
        Map<String, WebSocketUserInfo> onlineSession = WebSocketUtils.getOnlineSession();
        for (Map.Entry<String, WebSocketUserInfo> entry : onlineSession.entrySet()) {
            WebSocketUserInfo userInfo=entry.getValue();
            if(userInfo.getNovelId().equals(novelId)&&userInfo.getChapterId().equals(chapterId)){
                //发送消息
                WebSocketPojo webSocketPojo=new WebSocketPojo();
                webSocketPojo.setAccountName(userInfo.getAccountName());
                webSocketPojo.setUsername(userInfo.getUsername());
                webSocketPojo.setMessageTime(new Date());
                webSocketPojo.setMessage(message);
                webSocketPojo.setMsgType(type);
                WebSocketUtils.sendMessage(userInfo.getSession(),webSocketPojo);
            }
        }
    }

    /**
     * 发送当前在线人数
     * @param novelId
     * @param chapterId
     */
    private void sendMessageOnlineNum(String novelId,String chapterId){
        String num=String.valueOf(WebSocketUtils.getOnlineSession().size());
        sendMessageForNovelIdAndChapterId(novelId,chapterId,num, MessageType.ONLINE_NUM.getCode());
    }
}
