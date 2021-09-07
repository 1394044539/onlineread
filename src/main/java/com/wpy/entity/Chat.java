package com.wpy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wpy
 * @since 2021-04-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("chat")
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private String id;

    /**
     * 消息内容
     */
    @TableField("message")
    private String message;

    /**
     * 发送人
     */
    @TableField("send_user_id")
    private String sendUserId;

    /**
     * 发送时间
     */
    @TableField("send_time")
    private Date sendTime;

    /**
     * 小说id
     */
    @TableField("novel_id")
    private String novelId;

    /**
     * 章节id
     */
    @TableField("chapter_id")
    private String chapterId;

    /**
     * 0,"通知消息";1,"聊天消息";2,"用户自己";3,"在线人数"
     */
    @TableField("msg_type")
    private Integer msgType;

    public Chat() {
    }

    public Chat(String id, String message, String sendUserId, Date sendTime, String novelId, String chapterId, Integer msgType) {
        this.id = id;
        this.message = message;
        this.sendUserId = sendUserId;
        this.sendTime = sendTime;
        this.novelId = novelId;
        this.chapterId = chapterId;
        this.msgType = msgType;
    }
}
