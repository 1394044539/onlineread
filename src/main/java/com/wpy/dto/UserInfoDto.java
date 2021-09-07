package com.wpy.dto;

import lombok.Data;

/**
 * 用户信息dto
 * @author pywang6
 */
@Data
public class UserInfoDto {

    private String accountName;

    private String username;

    private String token;

    private Integer roleType;
}
