package com.wpy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.dto.SysNoticeDto;
import com.wpy.entity.SysNotice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wpy.entity.SysUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wpy
 * @since 2021-03-24
 */
public interface SysNoticeService extends IService<SysNotice> {

    /**
     * 新增公告
     * @param sysUser
     * @param sysNotice
     */
    void addNotice(SysUser sysUser, SysNotice sysNotice);

    /**
     * 修改公告
     * @param sysUser
     * @param sysNotice
     */
    void editNotice(SysUser sysUser, SysNotice sysNotice);

    /**
     * 获取公告列表
     * @param sysNoticeDto
     * @return
     */
    Page<SysNotice> getList(SysNoticeDto sysNoticeDto);


    /**
     * 关闭公告
     * @param id
     */
    void closeNotice(String id);

    /**
     * 打开公告
     * @param id
     */
    void openNotice(String id);

    /**
     * 删除公告
     * @param ids
     */
    void deleteNotice(List<String> ids);
}
