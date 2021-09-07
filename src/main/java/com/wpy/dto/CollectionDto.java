package com.wpy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @author 13940
 * @date 2021/3/4
 */
@Data
public class CollectionDto extends AbstractSplitPageDto{

    /**
     * 收藏渠道(0:用户上传；1：平台上传;2:全部)
     */
    private Integer collectChannel;

    /**
     * 小说标题
     */
    private String novelTitle;

    /**
     * 用户名
     */
    private String username;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String endTime;

    /**
     * 小说id
     */
    private String novelId;

    /**
     * 收藏id
     */
    private String id;

    /**
     * 批量id
     */
    private List<String> ids;
}
