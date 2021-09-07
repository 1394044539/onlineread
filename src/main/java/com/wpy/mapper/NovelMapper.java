package com.wpy.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.dto.NovelDto;
import com.wpy.entity.Novel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 小说数据表 Mapper 接口
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
public interface NovelMapper extends BaseMapper<Novel> {

    /**
     * 查询类型
     * @param dto
     * @param page
     * @return
     */
    List<Novel> selectNovelList(@Param("dto") NovelDto dto, Page<Novel> page);
}
