package com.wpy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.dto.CollectionDto;
import com.wpy.entity.Novel;
import com.wpy.entity.SysUser;
import com.wpy.entity.UserCollection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wpy.pojo.NovelTreePojo;

import java.util.List;

/**
 * <p>
 * 收藏表 服务类
 * </p>
 *
 * @author wpy
 * @since 2020-11-26
 */
public interface UserCollectionService extends IService<UserCollection> {

    /**
     * 用户收藏
     * @param sysUser
     * @param novel
     * @param code
     * @return
     */
    UserCollection saveCollection(SysUser sysUser, Novel novel, Integer code);

    /**
     * 收藏列表
     * @param sysUser
     * @param collectionDto
     * @return
     */
    Page<UserCollection> queryPersonCollect(SysUser sysUser, CollectionDto collectionDto);

    /**
     * 添加到收藏
     * @param sysUser
     * @param novelId
     */
    Boolean addCollection(SysUser sysUser, String novelId);

    /**
     * 取消收藏小说
     * @param sysUser
     * @param novelId
     */
    void cancelCollection(SysUser sysUser, String novelId);

    /**
     * 获取树形的个人用户列表
     * @param sysUser
     * @param novelTreePojo
     * @return
     */
    List<NovelTreePojo> queryPersonTreeCollect(SysUser sysUser, NovelTreePojo novelTreePojo);

    /**
     * 获取收藏信息
     * @param sysUser
     * @param collectionDto
     * @return
     */
    UserCollection getCollection(SysUser sysUser, CollectionDto collectionDto);
}
