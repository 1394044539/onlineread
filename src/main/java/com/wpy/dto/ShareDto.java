package com.wpy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wpy.entity.ShareFile;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author 13940
 */

@Data
public class ShareDto extends AbstractSplitPageDto{

    /**
     * id
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 分享的内容
     */
    private List<ShareFile> shareFileList;

    /**
     * 失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date invalidTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 分享名
     */
    private String shareName;

    /**
     * 分享地址
     */
    private String sharePath;

    /**
     * 分享类型：0：随意进入;1:纯密码进入;2：指定用户分享;3:指定用户密码分享
     */
    private Integer shareType;

    /**
     * 分享状态: 0:有效;1:删除;2:禁用
     */
    private Integer shareStatus;

    /**
     * 被分享人
     */
    private String shareUser;

    /**
     * 被分享人集合
     */
    private List<String> shareUserList;

    /**
     * 密码
     */
    private String sharePwd;
}
