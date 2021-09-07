package com.wpy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wpy
 * @since 2021-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("share_file")
public class ShareFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private String id;

    /**
     * 分享id
     */
    @TableField("share_id")
    private String shareId;

    /**
     * 小说或者文件夹id
     */
    @TableField("file_id")
    private String fileId;

    /**
     * 分享的文件类型:0:文件夹;1:小说
     */
    @TableField("file_type")
    private Integer fileType;


}
