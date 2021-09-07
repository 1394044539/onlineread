package com.wpy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 小说数据表
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("novel")
public class Novel implements Serializable {

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
     * 文件id
     */
    @TableField("file_id")
    private String fileId;

    /**
     * 总行数
     */
    @TableField("novel_total")
    private Long novelTotal;

    /**
     * 总字数
     */
    @TableField("novel_word_num")
    private Long novelWordNum;

    /**
     * 标题(设置50防止某些莫名其妙的小说名)
     */
    @TableField("novel_title")
    private String novelTitle;

    /**
     * 封面
     */
    @TableField("novel_img")
    private String  novelImg;

    /**
     * 简介
     */
    @TableField("novel_introduce")
    private String novelIntroduce;

    /**
     * 热度
     */
    @TableField("novel_hot")
    private Long novelHot;

    /**
     * 点击量
     */
    @TableField("novel_click")
    private Long novelClick;

    /**
     * 评论数
     */
    @TableField("novel_comment")
    private Integer novelComment;

    /**
     * 收藏数
     */
    @TableField("novel_collection")
    private Integer novelCollection;

    /**
     * 分享数
     */
    @TableField("novel_share")
    private String novelShare;

    /**
     * 点赞数
     */
    @TableField("novel_up")
    private Integer novelUp;

    /**
     * 类型(可多选)
     */
    @TableField("novel_type")
    private String novelType;

    @TableField(exist = false)
    private List<SysDict> novelTypeDict;

    @TableField(exist = false)
    private List<SysDict> novelTypes;

    /**
     * 小说作者
     */
    @TableField("novel_author")
    private String novelAuthor;

    /**
     * 小说发布日期
     */
    @TableField("novel_public_date")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date novelPublicDate;

    /**
     * 上传类型
     */
    @TableField("novel_upload_type")
    private Integer novelUploadType;

    /**
     * 上传用户id
     */
    @TableField("upload_user_id")
    private String uploadUserId;

    @TableField(exist = false)
    private String uploadUsername;

    @TableField(exist = false)
    private String uploadAccountName;

    @TableField(exist = false)
    private String roleType;

    /**
     * 小说状态:审核状态:0:未审核；1:待审核;2:审核完成;3：审核失败，4：被禁用
     */
    @TableField("novel_status")
    private Integer novelStatus;

    @TableField("error_msg")
    private String errorMsg;

    /**
     * 是否收藏
     */
    @TableField(exist = false)
    private Integer isCollection;

    /**
     * 章节列表
     */
    @TableField(exist = false)
    private List<NovelChapter> chapterList;

    @TableField(exist = false)
    private String chapterNum;

}
