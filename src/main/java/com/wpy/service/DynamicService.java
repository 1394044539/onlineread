package com.wpy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.dto.DynamicDto;
import com.wpy.entity.Dynamic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wpy.entity.Novel;
import com.wpy.entity.SysUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wpy
 * @since 2021-04-09
 */
public interface DynamicService extends IService<Dynamic> {

    /**
     * 上传小说后的动态
     * @param sysUser
     * @param uploadNovels
     */
    void addUploadDynamic(SysUser sysUser, List<Novel> uploadNovels);

    /**
     * 审核动态
     * @param sysUser
     * @param novels
     */
    void novelAudit(SysUser sysUser, List<Novel> novels);

    /**
     * 获取动态列表
     * @param sysUser
     * @param dynamicDto
     * @return
     */
    Page<Dynamic> getList(SysUser sysUser, DynamicDto dynamicDto);

    /**
     * 标记已读
     * @param sysUser
     * @param dynamicDto
     */
    void alread(SysUser sysUser, DynamicDto dynamicDto);

    /**
     * 删除动态
     * @param sysUser
     * @param dynamicDto
     */
    void deleteDynamic(SysUser sysUser, DynamicDto dynamicDto);
}
