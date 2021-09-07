package com.wpy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 小说上传的dto
 * @author 13940
 * @date 2021/3/2
 */

@Data
public class NovelUploadDto {

    /**
     * 小说id
     */
    private String id;
    /**
     * 小说标题
     */
    private String novelTitle;

    /**
     * 小说作者
     */
    private String novelAuthor;

    /**
     *时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date novelPublicDate;

    /**
     * 小说简介
     */
    private String novelIntroduce;

    /**
     * 小说类型
     */
    private List<String> novelTypes;

    /**
     * 小说封面
     */
    private MultipartFile imgFile;

    /**
     * 小说文件
     */
    private MultipartFile[] novelFiles;

    /**
     * 是否自动收藏
     */
    private Boolean isCollect;

    /**
     * 是否发布
     */
    private Boolean isPublic;

}
