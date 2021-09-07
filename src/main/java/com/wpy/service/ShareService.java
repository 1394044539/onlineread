package com.wpy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.dto.ShareDto;
import com.wpy.entity.Share;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wpy.entity.SysUser;
import com.wpy.pojo.NovelTreePojo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wpy
 * @since 2021-03-30
 */
public interface ShareService extends IService<Share> {

    /**
     * 创建分享
     * @param sysUser
     * @param shareDto
     * @return
     */
    Share addShare(SysUser sysUser, ShareDto shareDto);

    /**
     * 修改分享
     * @param sysUser
     * @param shareDto
     */
    void editShare(SysUser sysUser, ShareDto shareDto);

    /**
     * 获取分享列表
     * @param sysUser
     * @param shareDto
     * @return
     */
    Page<Share> getShareList(SysUser sysUser, ShareDto shareDto);

    /**
     * 获取分享后的文件列表
     * @param sysUser
     * @param shareDto
     * @return
     */
    List<NovelTreePojo> shareFileList(SysUser sysUser, ShareDto shareDto);

    /**
     * 校验是否需要登录或者密码或者是否失效
     *
     * @param shareDto
     * @return
     */
    Integer checkNeedPwd(ShareDto shareDto);

    /**
     * 校验密码和用户是否正确
     * @param sysUser
     * @param shareDto
     * @return
     */
    Integer checkPwd(SysUser sysUser, ShareDto shareDto);
}
