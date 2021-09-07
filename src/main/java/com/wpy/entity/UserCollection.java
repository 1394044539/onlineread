package com.wpy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 收藏表
 * </p>
 *
 * @author wpy
 * @since 2020-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_collection")
public class UserCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private String id;

    /**
     * 创建人
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 小说id
     */
    @TableField("novel_id")
    private String novelId;

    /**
     * 目录id
     */
    @TableField("catalog_id")
    private String catalogId;

    @TableField(exist = false)
    private String catalogName;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    @TableField(exist = false)
    private String username;

    /**
     * 收藏渠道(0:用户上传；1：平台上传)
     */
    @TableField("collect_channel")
    private Integer collectChannel;

    @TableField(exist = false)
    private Novel novel;

    public UserCollection(){}

    public UserCollection(String uuid, String accountName, Date date) {
        this.id=uuid;
        this.createBy=accountName;
        this.createTime=date;
    }
}
