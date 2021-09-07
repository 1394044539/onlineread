package com.wpy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author 13940
 * @date 2021/3/4
 */
@Data
public class NovelDto extends AbstractSplitPageDto{

    /**
     * 主键id
     */
    private String id;

    private List<String> ids;

    private String userId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 总行数
     */
    private Integer novelTotal;

    /**
     * 标题(设置50防止某些莫名其妙的小说名)
     */
    private String novelTitle;

    /**
     * 类型
     */
    private String novelType;

    /**
     * 类型
     */
    private List<String> novelTypes;

    /**
     * 小说作者
     */
    private String novelAuthor;

    /**
     * 小说发布日期
     */
    private Date novelPublicDate;

    /**
     * 是否为个人
     */
    private Integer isPerson;

    /**
     * 上传用户id
     */
    private String uploadUserId;

    /**
     * 发布类型(0:公开;1:私有)
     */
    private Integer novelUploadType;

    /**
     * 排序类型（0：热度，1：时间）
     */
    private Integer sortType;

    /**
     *页面类型(0:首页；1：用户个人页面：2管理员页面)
     */
    private Integer pageType;

    /**
     * 审核状态
     */
    private Integer novelStatus;

    /**
     * 审核失败或禁用原因
     */
    private String errorMsg;
}
