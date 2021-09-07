package com.wpy.pojo;

import lombok.Data;

/**
 * @author pywang6
 * 收藏信息
 */
@Data
public class UserCollectFileInfoPojo {

    /**
     * id
     */
    private String id;

    private String novelTitle;

    private String filePath;

    private String fileSize;

    private String fileType;

    private String fileName;


}
