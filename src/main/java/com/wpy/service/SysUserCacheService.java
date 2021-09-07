package com.wpy.service;

import com.wpy.entity.SysUser;

/**
 * @author 13940
 */
public interface SysUserCacheService {

    /**
     * 初始化用户信息
     */
    void initUserInfo();

    /**
     * 获得用户信息
     * @param userId
     * @return
     */
    SysUser getSysUser(String userId);

    /**
     * 获得账号
     * @param userId
     * @return
     */
    String getAccountName(String userId);

    /**
     * 获得用户名
     * @param userId
     * @return
     */
    String getUsername(String userId);

    /**
     * 设置用户信息
     * @param sysUser
     */
     void setUserInfo(SysUser sysUser);

    /**
     * 设置用户信息
     * @param userId
     */
    void setUserInfo(String userId);
}
