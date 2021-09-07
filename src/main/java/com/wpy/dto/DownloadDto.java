package com.wpy.dto;

import lombok.Data;

import java.util.List;

/**
 * @author pywang6
 * @date 20213/29
 */
@Data
public class DownloadDto {

    /**
     * id
     */
    private String id;

    /**
     * 类型：0：文件夹，1：小说
     */
    private Integer type;

    /**
     * 集合
     */
    private List<DownloadDto> list;

    /**
     * 下载的类型：1：只下载小说，2：我全都要
     */
    private Integer downloadType;
}
