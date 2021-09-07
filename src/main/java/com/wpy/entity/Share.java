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
 * @since 2021-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("share")
public class Share implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private String id;

    /**
     * 分享名
     */
    @TableField("share_name")
    private String shareName;

    /**
     * 分享人id
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 被分享人
     */
    @TableField("share_user")
    private String shareUser;

    /**
     * 分享密码
     */
    @TableField("share_pwd")
    private String sharePwd;

    /**
     * 分享连接
     */
    @TableField("share_path")
    private String sharePath;

    /**
     * 失效时间
     */
    @TableField("invalid_time")
    private Date invalidTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 分享类型：0：随意进入;1:纯密码进入;2：指定用户分享;3:指定用户密码分享
     */
    @TableField("share_type")
    private Integer shareType;

    /**
     * 分享状态: 0:有效;1:删除;2:禁用
     */
    @TableField("share_status")
    private Integer shareStatus;


}
