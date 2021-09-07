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
 * 字典数据表
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_dict")
public class SysDict implements Serializable {

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
     * 字典大类
     */
    @TableField("dict_class")
    private String dictClass;

    /**
     * 字典key值
     */
    @TableField("dict_key")
    private String dictKey;

    /**
     * 字典value值
     */
    @TableField("dict_value")
    private String dictValue;

    /**
     * 字典排序
     */
    @TableField("order_num")
    private String orderNum;

    /**
     * 父字典key值
     */
    @TableField("parent_key")
    private String parentKey;

    /**
     *备注
     */
    @TableField("remarks")
    private String remarks;
}
