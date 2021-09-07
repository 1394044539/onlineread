package com.wpy.dto;

import lombok.Data;

/**
 * @author pywang6
 * @date 2021/05/07
 */
@Data
public class SysDictDto extends AbstractSplitPageDto{

    /**
     * id
     */
    private String id;

    /**
     * 字典大类
     */
    private String dictClass;

    /**
     * 字典key值
     */
    private String dictKey;

    /**
     * 字典value值
     */
    private String dictValue;

    /**
     * 排序
     */
    private String orderNum;

    private String order;

    /**
     * 备注
     */
    private String remarks;
}
