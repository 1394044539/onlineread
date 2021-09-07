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
 * 用户数据表
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
public class SysUser implements Serializable {

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
     * 账号
     */
    @TableField("account_name")
    private String accountName;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 身份证号
     */
    @TableField("identity_card")
    private String identityCard;

    /**
     * 真实姓名
     */
    @TableField("true_name")
    private String trueName;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 用户头像
     */
    @TableField("photo")
    private String photo;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 角色类型
     */
    @TableField("role_type")
    private Integer roleType;

    /**
     * 用户状态(0:正常;1:注销;2:禁用)
     */
    @TableField("status")
    private Integer status;


}
