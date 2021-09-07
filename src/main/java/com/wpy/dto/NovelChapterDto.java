package com.wpy.dto;

import lombok.Data;

/**
 * @author 13940
 * @date 2021/37
 */
@Data
public class NovelChapterDto {

    /**
     * 小说id
     */
    private String novelId;

    /**
     * 章节id
     */
    private String chapterId;

    /**
     * 开始行数
     */
    private Long chapterLine;

    /**
     * 本章行数
     */
    private Long chapterTotal;

    /**
     * 章节名称
     */
    private String chapterName;
}
