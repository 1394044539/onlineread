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
 * 评论表
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("conmment")
public class Conmment implements Serializable {

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
     * 评论内容
     */
    @TableField("content")
    private String content;

    /**
     * 父评论id
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 点赞数
     */
    @TableField("comment_up")
    private Integer commentUp;

    /**
     * 评论数
     */
    @TableField("comment_num")
    private Integer commentNum;


}
