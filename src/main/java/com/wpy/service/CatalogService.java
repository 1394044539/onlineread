package com.wpy.service;

import com.wpy.dto.CatalogDto;
import com.wpy.entity.Catalog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wpy.entity.SysUser;

/**
 * <p>
 * 目录表 服务类
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
public interface CatalogService extends IService<Catalog> {

    /**
     * 创建文件夹
     * @param sysUser
     * @param catalog
     */
    void createCatalog(SysUser sysUser, Catalog catalog);

    /**
     * 移动或复制文件夹
     * @param sysUser
     * @param catalogDto
     */
    void moveOrCopyCatalog(SysUser sysUser, CatalogDto catalogDto);

    /**
     * 复制文件夹
     * @param sysUser
     * @param catalogDto
     */
    void copyCatalog(SysUser sysUser, CatalogDto catalogDto);

    /**
     * 移动文件夹
     * @param sysUser
     * @param catalogDto
     */
    void moveCatalog(SysUser sysUser, CatalogDto catalogDto);

    /**
     * 移动或复制小说
     * @param sysUser
     * @param catalogDto
     */
    void moveOrCopyNovel(SysUser sysUser, CatalogDto catalogDto);

    /**
     * 复制小说
     * @param sysUser
     * @param catalogDto
     */
    void copyNovel(SysUser sysUser,CatalogDto catalogDto);

    /**
     * 移动小说
     * @param sysUser
     * @param catalogDto
     */
    void moveNovel(SysUser sysUser,CatalogDto catalogDto);

    /**
     * 删除文件夹
     * @param sysUser
     * @param catalogDto
     */
    void deleteCatalog(SysUser sysUser, CatalogDto catalogDto);

    /**
     * 更新文件及名称
     * @param sysUser
     * @param catalogDto
     */
    void updateCatalog(SysUser sysUser, CatalogDto catalogDto);
}
