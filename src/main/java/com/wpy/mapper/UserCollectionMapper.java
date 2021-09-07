package com.wpy.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.dto.CatalogDto;
import com.wpy.dto.CollectionDto;
import com.wpy.entity.UserCollection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wpy.pojo.NovelTreePojo;
import com.wpy.pojo.UserCollectFileInfoPojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 收藏表 Mapper 接口
 * </p>
 *
 * @author wpy
 * @since 2020-11-26
 */
public interface UserCollectionMapper extends BaseMapper<UserCollection> {

    /**
     * 获得小说列表
     * @param userid
     * @param collectionDto
     * @param page
     * @return
     */
    Page<UserCollection> queryPersonCollect(@Param("userid") String userid, @Param("collectionDto") CollectionDto collectionDto,Page<UserCollection> page);

    /**
     * 获得小说列表
     * @param userid
     * @param novelTreePojo
     * @return
     */
    List<UserCollection> queryPersonCollectList(@Param("userid") String userid, @Param("pojo") NovelTreePojo novelTreePojo);

    /**
     * 根据id更新目录
     * @param catalogDto
     */
    void updateCatalogIdNull(@Param("catalogDto") CatalogDto catalogDto);

    /**
     * 通过catalogId查询文件信息
     *
     * @param catalogId
     * @param userId
     * @return
     */
    List<UserCollectFileInfoPojo> selectFileInfoByCatalogId(@Param("catalogId") String catalogId, @Param("userId") String userId);
}
