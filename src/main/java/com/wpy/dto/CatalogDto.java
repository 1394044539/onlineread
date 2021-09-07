package com.wpy.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author pywang6
 * @date
 */

@Data
public class CatalogDto {

    /**
     * 选择id
     */
    private String choseId;

    /**
     * id集合
     */
    private List<String> ids;

    /**
     * 目标id
     */
    private String targetId;

    /**
     * 判断是复制还是移动
     */
    private Integer copyOrMove;

    /**
     * 更新日期
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 目录名称
     */
    private String catalogName;
}
