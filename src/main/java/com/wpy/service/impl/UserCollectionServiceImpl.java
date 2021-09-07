package com.wpy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wpy.constant.SqlConstant;
import com.wpy.dto.CollectionDto;
import com.wpy.entity.Catalog;
import com.wpy.entity.Novel;
import com.wpy.entity.SysUser;
import com.wpy.entity.UserCollection;
import com.wpy.enums.TypeEnums;
import com.wpy.mapper.CatalogMapper;
import com.wpy.mapper.UserCollectionMapper;
import com.wpy.pojo.NovelTreePojo;
import com.wpy.service.NovelService;
import com.wpy.service.UserCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wpy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 收藏表 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2020-11-26
 */
@Service
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection> implements UserCollectionService {

    @Autowired
    private UserCollectionMapper userCollectionMapper;
    @Autowired
    private NovelService novelService;
    @Autowired
    private CatalogMapper catalogMapper;

    @Override
    public UserCollection saveCollection(SysUser sysUser, Novel novel, Integer code) {
        UserCollection userCollection=new UserCollection(StringUtils.getUuid(),sysUser.getAccountName(),new Date());
        userCollection.setNovelId(novel.getId());
        userCollection.setCollectChannel(code);
        userCollection.setUserId(sysUser.getId());
        this.save(userCollection);
        return userCollection;
    }

    @Override
    public Page<UserCollection> queryPersonCollect(SysUser sysUser, CollectionDto collectionDto) {
        return userCollectionMapper.queryPersonCollect(sysUser.getId(), collectionDto, new Page<UserCollection>(collectionDto.getPageNum(), collectionDto.getPageSize()));
    }

    @Override
    public Boolean addCollection(SysUser sysUser, String novelId) {
        //先判断自己有没有收藏
        UserCollection userCollection = userCollectionMapper.selectOne(new QueryWrapper<UserCollection>()
                .eq(SqlConstant.USER_ID, sysUser.getId())
                .eq(SqlConstant.NOVEL_ID,novelId));
        if(userCollection!=null){
            return true;
        }
        Novel novel = novelService.getById(novelId);

        //判断收藏的小说是自己的还是平台的
        Integer code= novel.getUploadUserId().equals(sysUser.getId())?TypeEnums.USER_CHANNEL.getCode():TypeEnums.SYS_CHANNEL.getCode();
        this.saveCollection(sysUser,novel,code);
        return null;
    }

    @Override
    public void cancelCollection(SysUser sysUser, String novelId) {
        this.remove(new QueryWrapper<UserCollection>().eq(SqlConstant.NOVEL_ID,novelId).eq(SqlConstant.USER_ID,sysUser.getId()));
    }

    @Override
    public List<NovelTreePojo> queryPersonTreeCollect(SysUser sysUser, NovelTreePojo novelTreePojo) {
        List<Catalog> catalogs=Lists.newArrayList();
        List<UserCollection> userCollections=Lists.newArrayList();
        //1、查询目录表
        //判断是否有父级id
        if(TypeEnums.CATALOG_TYPE.getCode().equals(novelTreePojo.getType())||TypeEnums.ALL_TYPE.getCode().equals(novelTreePojo.getType())){
            //如果选择CatalogType，则只有文件夹
            QueryWrapper<Catalog> qw = new QueryWrapper<>();
            qw.eq(SqlConstant.USER_ID, sysUser.getId());
            if(StringUtils.isNotEmpty(novelTreePojo.getName())){
                qw.like(SqlConstant.CATALOG_NAME,novelTreePojo.getName());
            }
            if(StringUtils.isEmpty(novelTreePojo.getParentId())){
                //拿第一层
                qw.isNull(SqlConstant.PARENT_ID);
                catalogs = this.catalogMapper.selectList(qw);
            }else{
                //往后面拿
                qw.eq(SqlConstant.PARENT_ID,novelTreePojo.getParentId());
                catalogs = this.catalogMapper.selectList(qw);
            }
        }
        //2、查询收藏表
        if(TypeEnums.NOVEL_TYPE.getCode().equals(novelTreePojo.getType())||TypeEnums.ALL_TYPE.getCode().equals(novelTreePojo.getType())){
            //如果是选择NovelType，则只有小说
            userCollections=this.userCollectionMapper.queryPersonCollectList(sysUser.getId(), novelTreePojo);
        }
        //3、封装参数
        List<NovelTreePojo> list=this.getPojoList(catalogs,userCollections,novelTreePojo.getParentId());
        return list;
    }

    @Override
    public UserCollection getCollection(SysUser sysUser, CollectionDto collectionDto) {
        if(StringUtils.isEmpty(sysUser.getId())){
            //说明没登录，直接返回null
            return null;
        }
        //登陆了，按照id或者novelId找
        QueryWrapper<UserCollection> qw=new QueryWrapper<UserCollection>();
        qw.eq(SqlConstant.USER_ID,sysUser.getId());
        if(StringUtils.isNotEmpty(collectionDto.getId())){
            qw.eq(SqlConstant.ID,collectionDto.getId());
        }
        if(StringUtils.isNotEmpty(collectionDto.getNovelId())){
            qw.eq(SqlConstant.NOVEL_ID,collectionDto.getNovelId());
        }
        return this.getOne(qw);
    }

    private List<NovelTreePojo> getPojoList(List<Catalog> catalogs, List<UserCollection> collections, String parentId) {
        List<NovelTreePojo> list = Lists.newArrayList();
        for (Catalog catalog : catalogs) {
            NovelTreePojo novelTreePojo=new NovelTreePojo();
            novelTreePojo.setId(catalog.getId());
            novelTreePojo.setType(TypeEnums.CATALOG_TYPE.getCode());
            novelTreePojo.setName(catalog.getCatalogName());
            novelTreePojo.setParentId(parentId);
            novelTreePojo.setChildren(Lists.newArrayList());
            list.add(novelTreePojo);
        }
        for (UserCollection collection : collections) {
            NovelTreePojo novelTreePojo=new NovelTreePojo();
            //可能为空
            if(StringUtils.isNotBlank(collection.getNovel())){
                novelTreePojo.setNovelId(collection.getNovel().getId());
                novelTreePojo.setName(collection.getNovel().getNovelTitle());
                novelTreePojo.setNovelImg(collection.getNovel().getNovelImg());
                novelTreePojo.setNovelStatus(collection.getNovel().getNovelStatus());
            }
            novelTreePojo.setId(collection.getId());
            novelTreePojo.setParentId(parentId);
            novelTreePojo.setType(TypeEnums.NOVEL_TYPE.getCode());
            novelTreePojo.setCollectChannel(collection.getCollectChannel());
            list.add(novelTreePojo);
        }
        return list;
    }
}
