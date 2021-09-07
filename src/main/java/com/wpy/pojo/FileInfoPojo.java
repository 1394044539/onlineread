package com.wpy.pojo;

import lombok.Data;

import java.util.List;

/**
 * 文件信息
 * @author 13940
 * @date 2021/3/4
 */
@Data
public class FileInfoPojo {

    /**
     * 总行数
     */
    private Long total;

    /**
     * 总字数
     */
    private Long wordNum;

    /**
     * 全部数据
     */
    private List<String> allList;

    /**
     * 指定数据
     */
    private List<String> appointList;
}
