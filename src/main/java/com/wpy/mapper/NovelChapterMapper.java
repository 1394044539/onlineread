package com.wpy.mapper;

import com.wpy.dto.NovelChapterDto;
import com.wpy.entity.NovelChapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wpy.pojo.NovelChapterPojo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 章节表 Mapper 接口
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
public interface NovelChapterMapper extends BaseMapper<NovelChapter> {

    /**
     * 获取根据章节获取文件的信息
     * @param novelChapterDto
     * @return
     */
    NovelChapterPojo getChapterContext(@Param("novelChapterDto") NovelChapterDto novelChapterDto);
}
