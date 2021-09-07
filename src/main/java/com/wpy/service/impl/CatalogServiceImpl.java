package com.wpy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.wpy.constant.SqlConstant;
import com.wpy.dto.CatalogDto;
import com.wpy.entity.Catalog;
import com.wpy.entity.SysUser;
import com.wpy.entity.UserCollection;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.enums.TypeEnums;
import com.wpy.exception.BusinessException;
import com.wpy.mapper.CatalogMapper;
import com.wpy.mapper.UserCollectionMapper;
import com.wpy.service.CatalogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wpy.service.UserCollectionService;
import com.wpy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 目录表 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@Service
public class CatalogServiceImpl extends ServiceImpl<CatalogMapper, Catalog> implements CatalogService {

    @Autowired
    private UserCollectionService userCollectionService;
    @Autowired
    private UserCollectionMapper userCollectionMapper;
    @Autowired
    private CatalogMapper catalogMapper;

    @Override
    public void createCatalog(SysUser sysUser, Catalog catalog) {
        Catalog newCatalog=new Catalog();
        newCatalog.setId(StringUtils.getUuid());
        newCatalog.setCatalogName(catalog.getCatalogName());
        newCatalog.setCreateBy(sysUser.getId());
        newCatalog.setCreateTime(new Date());
        if(StringUtils.isNotEmpty(catalog.getParentId())){
            newCatalog.setParentId(catalog.getParentId());
        }
        newCatalog.setUserId(sysUser.getId());
        this.save(newCatalog);
    }

    @Override
    public void moveOrCopyCatalog(SysUser sysUser, CatalogDto catalogDto) {
        if(TypeEnums.MOVE_TYPE.getCode().equals(catalogDto.getCopyOrMove())){
            //移动
            this.moveCatalog(sysUser,catalogDto);
        }
    }


    @Override
    public void copyCatalog(SysUser sysUser, CatalogDto catalogDto) {

        Catalog catalog = this.getById(catalogDto.getChoseId());
        if(StringUtils.isEmpty(catalogDto.getTargetId())){
            //如果目标id为空，则默认是往第一层复制

        }
    }

    @Override
    public void moveCatalog(SysUser sysUser, CatalogDto catalogDto) {
        catalogDto.setUpdateBy(sysUser.getId());
        catalogDto.setUpdateTime(new Date());
        this.catalogMapper.updateCatalogIdNull(catalogDto);
    }

    @Override
    public void moveOrCopyNovel(SysUser sysUser, CatalogDto catalogDto) {
        if(TypeEnums.MOVE_TYPE.getCode().equals(catalogDto.getCopyOrMove())){
            this.moveNovel(sysUser,catalogDto);
        }
    }

    @Override
    public void copyNovel(SysUser sysUser, CatalogDto catalogDto) {

    }

    @Override
    public void moveNovel(SysUser sysUser, CatalogDto catalogDto) {
        catalogDto.setUpdateBy(sysUser.getId());
        catalogDto.setUpdateTime(new Date());
        userCollectionMapper.updateCatalogIdNull(catalogDto);
    }

    @Override
    public void deleteCatalog(SysUser sysUser, CatalogDto catalogDto) {
        List<String> ids= Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(catalogDto.getIds())){
            ids.addAll(catalogDto.getIds());
        }
        if(StringUtils.isNotEmpty(catalogDto.getChoseId())){
            ids.add(catalogDto.getChoseId());
        }
        if(CollectionUtils.isEmpty(ids)){
            return;
        }
        //1、删除这个目录
        this.removeByIds(ids);
        //2、删除该目录下的所有收藏记录
        QueryWrapper<UserCollection> qw=new QueryWrapper<UserCollection>();
        qw.in(SqlConstant.NOVEL_ID,ids);
        this.userCollectionMapper.delete(qw);
    }

    @Override
    public void updateCatalog(SysUser sysUser, CatalogDto catalogDto) {
        UpdateWrapper<Catalog> uw=new UpdateWrapper<>();
        uw.set(SqlConstant.CATALOG_NAME,catalogDto.getCatalogName());
        uw.eq(SqlConstant.ID,catalogDto.getChoseId());
        this.update(uw);
    }

}
