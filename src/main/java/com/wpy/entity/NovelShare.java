package com.wpy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 小说分享表
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("novel_share")
public class NovelShare implements Serializable {

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
     * 更新人
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 小说id
     */
    @TableField("novel_id")
    private String novelId;

    /**
     * 分享路径
     */
    @TableField("share_path")
    private String sharePath;

    /**
     * 分享密码
     */
    @TableField("share_pwd")
    private String sharePwd;

    /**
     * 分享名称
     */
    @TableField("share_name")
    private String shareName;

    /**
     * 分享有效期
     */
    @TableField("share_time")
    private Date shareTime;

    /**
     * 分享状态
     */
    @TableField("share_status")
    private Integer shareStatus;


}
