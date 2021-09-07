package com.wpy.pojo;

import com.wpy.entity.Novel;
import lombok.Data;

import java.util.Date;

@Data
public class EpubInfoPojo {

    /**
     * 作者
     */
    private String author;

    /**
     * 发布日期
     */
    private Date novelPublicDate;

    /**
     * 描述信息
     */
    private String novelIntroduce;

    /**
     * 小说标题
     */
    private String novelTitle;

    /**
     * 小说
     */
    private Novel novel;
}
