package com.wpy.mapper;

import com.wpy.dto.CatalogDto;
import com.wpy.entity.Catalog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 目录表 Mapper 接口
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
public interface CatalogMapper extends BaseMapper<Catalog> {

    /**
     * 移动文件夹
     * @param catalogDto
     */
    void updateCatalogIdNull(@Param("catalogDto") CatalogDto catalogDto);
}
