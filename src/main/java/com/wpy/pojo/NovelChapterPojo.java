package com.wpy.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author 13940
 * @date 2021/3/7
 */
@Data
public class NovelChapterPojo {

    /**
     * 文字内容
     */
    private List<String> list;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 章节名称
     */
    private String chapterName;

    /**
     * 小说id
     */
    private String novelId;

    /**
     * 小说名字
     */
    private String novelTitle;

    /**
     * 章节id
     */
    private String chapterId;

    /**
     * 上一章id
     */
    private String lastChapterId;

    /**
     * 下一章id
     */
    private String nextChapterId;

    /**
     * 章节排序
     */
    private Integer chapterOrder;

    /**
     * 开始行数
     */
    private Long chapterLine;

    /**
     * 本章行数
     */
    private Long chapterTotal;

    /**
     * epub的路径
     */
    private String chapterHref;
}
