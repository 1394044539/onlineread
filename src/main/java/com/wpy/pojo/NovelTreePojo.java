package com.wpy.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author 13940
 * @date 2021/3/28
 */

@Data
public class NovelTreePojo {

    /**
     * 可能是文件夹id，可能是收藏的id
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 小说id
     */
    private String novelId;

    /**
     * 名字
     */
    private String name;

    /**
     * 类型：0文件夹，1小说，2全部
     */
    private Integer type;

    /**
     * 小说封面
     */
    private String novelImg;

    /**
     * 父级Id
     */
    private String parentId;

    /**
     * 子集数据
     */
    private List<NovelTreePojo> children;

    /**
     * 类型
     */
    private Integer collectChannel;

    /**
     * 小说状态
     */
    private Integer novelStatus;
}
