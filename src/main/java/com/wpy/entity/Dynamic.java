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
 * @since 2021-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dynamic")
public class Dynamic extends AbstractUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private String id;

    /**
     * 动态标题
     */
    @TableField("dynamic_title")
    private String dynamicTitle;

    /**
     * 动态内容
     */
    @TableField("dynamic_content")
    private String dynamicContent;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 创建人id
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 是否已阅读:0:未阅读，1：已阅读
     */
    @TableField("is_read")
    private Integer isRead;

    /**
     * 动态类型：0：小说审核；1：用户审核
     */
    @TableField("dynamic_type")
    private Integer dynamicType;


}
