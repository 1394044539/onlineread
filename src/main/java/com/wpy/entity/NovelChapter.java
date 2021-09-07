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
 * 章节表
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("novel_chapter")
public class NovelChapter implements Serializable {

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
     * 章节名
     */
    @TableField("chapter_name")
    private String chapterName;

    /**
     * 章节行
     */
    @TableField("chapter_line")
    private Long chapterLine;

    /**
     * 结束行数
     */
    @TableField(exist = false)
    private Long endLine;

    /**
     * 章节总行数
     */
    @TableField("chapter_total")
    private Long chapterTotal;

    /**
     * 章节排序
     */
    @TableField("chapter_order")
    private Integer chapterOrder;

    /**
     * epub的href
     */
    @TableField("chapter_href")
    private String chapterHref;

    /**
     *父目录id
     */
    @TableField("parent_id")
    private String parentId;

    public NovelChapter(){

    }

    public NovelChapter(String uuid, String accountName, Date date) {
        this.id=uuid;
        this.createTime=date;
        this.createBy=accountName;
        this.updateBy=accountName;
        this.updateTime=date;
    }
}
