package com.wpy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author 13940
 */
@Data
public class DynamicDto extends AbstractSplitPageDto {

    /**
     * id
     */
    private String id;

    private List<String> ids;

    /**
     * 标题
     */
    private String dynamicTitle;

    /**
     * 动态类型
     */
    private Integer dynamicType;

    /**
     * 是否阅读,0:未阅读，1：已阅读
     */
    private Integer isRead;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户账号
     */
    private String accountName;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 是不是管理员自己
     */
    private Boolean isAdmin;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
