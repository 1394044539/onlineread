package com.wpy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 阅读历史表
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@TableName("read_history")
public class ReadHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private String id;

    /**
     * 创建人
     */
    @TableField("user_id")
    private String userId;

    @TableField(exist = false)
    private String username;

    /**
     * 用户可能未登录
     */
    @TableField("ip")
    private String ip;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 小说id、
     */
    @TableField("novel_id")
    private String novelId;

    /**
     * 小说最后访问页数
     */
    @TableField("last_chapter_id")
    private String lastChapterId;

    /**
     * 记录途径（0：已登录；1：未登录）
     */
    @TableField("user_type")
    private Integer userType;

    /**
     * 阅读记录，纵坐标
     */
    @TableField("read_position")
    private Integer readPosition;

    /**
     * 页面的总纵坐标
     */
    @TableField("read_all_position")
    private Integer readAllPosition;

    /**
     * 书签名字
     */
    @TableField("book_mark_name")
    private String bookMarkName;

    /**
     *类型：0：历史记录；1：书签
     */
    @TableField("type")
    private Integer type;

    /**
     * 小说封面
     */
    @TableField(exist = false)
    private String novelImg;

    /**
     * 小说名字
     */
    @TableField(exist = false)
    private String novelTitle;

    /**
     * 小说
     */
    @TableField(exist = false)
    private Novel novel;

    /**
     * 小说章节
     */
    @TableField(exist = false)
    private NovelChapter novelChapter;

    @TableField(exist = false)
    private BigDecimal percentage;
}
