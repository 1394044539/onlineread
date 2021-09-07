package com.wpy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author 13940
 */

@Data
public abstract class AbstractUserInfo {

    @TableField(exist = false)
    private String userId;

    @TableField(exist = false)
    private String username;

    @TableField(exist = false)
    private String accountName;

    @TableField(exist = false)
    private String createAccountName;

    @TableField(exist = false)
    private String createUsername;

    @TableField(exist = false)
    private String updateAccountName;

    @TableField(exist = false)
    private String updateUsername;
}
