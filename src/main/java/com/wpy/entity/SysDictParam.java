package com.wpy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wpy
 * @since 2021-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_dict_param")
public class SysDictParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private String id;

    /**
     * 字典id
     */
    @TableField("sys_dict_id")
    private String sysDictId;

    /**
     * 参数key
     */
    @TableField("param_key")
    private String paramKey;

    /**
     * 参数值
     */
    @TableField("param_value")
    private String paramValue;

    /**
     * 排序
     */
    @TableField("order_num")
    private String orderNum;

    /**
     * 操作人
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 操作时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;


}
