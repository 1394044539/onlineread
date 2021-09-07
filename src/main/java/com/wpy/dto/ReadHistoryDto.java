package com.wpy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wpy.entity.SysUser;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 阅读历史dto
 * @author pywang6
 * @date 2021/3/12
 */

@Data
public class ReadHistoryDto extends AbstractSplitPageDto {

    /**
     * id
     */
    private String id;

    /**
     * 小说id
     */
    private String novelId;

    /**
     * 章节id
     */
    private String novelChaperId;

    /**
     * 当前位置
     */
    private Integer position;

    /**
     * 当前总坐标
     */
    private Integer allPosition;

    /**
     * 当前ip
     */
    private String ip;

    /**
     * 记录途径（0：已登录；1：未登录）
     */
    private Integer userType;

    /**
     * 类型：0：历史记录；1：书签
     */
    private Integer type;

    /**
     * 书签名
     */
    private String markName;

    /**
     * 用户id
     */
    private String userId;

    private String username;

    /**
     * 小说标题
     */
    private String novelTitle;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
