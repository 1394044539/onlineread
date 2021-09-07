package com.wpy.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.dto.ReadHistoryDto;
import com.wpy.entity.Novel;
import com.wpy.entity.ReadHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 阅读历史表 Mapper 接口
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
public interface ReadHistoryMapper extends BaseMapper<ReadHistory> {

    /**
     * 查询历史记录列表
     * @param readHistoryDto
     * @param type
     * @param objectPage
     * @return
     */
    Page<ReadHistory> queryHistory(@Param("dto") ReadHistoryDto readHistoryDto, @Param("type") Integer type, Page<Object> objectPage);


    /**
     * 查询历史记录列表，详细数据
     * @param readHistoryDto
     * @param objectPage
     * @return
     */
    Page<ReadHistory> queryHistoryInfo(@Param("dto") ReadHistoryDto readHistoryDto, Page<Object> objectPage);
}
