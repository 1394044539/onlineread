package com.wpy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_log")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private String id;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 操作者用户名
     */
    @TableField("operator_user_name")
    private String operatorUserName;

    /**
     * 操作者账号
     */
    @TableField("operator_account_name")
    private String operatorAccountName;

    /**
     * 请求方式
     */
    @TableField("method")
    private String method;

    /**
     * 参数
     */
    @TableField("param")
    private String param;

    /**
     * ip地址
     */
    @TableField("ip")
    private String ip;

    /**
     * 描述
     */
    @TableField("log_describe")
    private String logDescribe;

    /**
     * 请求路径
     */
    @TableField("path")
    private String path;


}
